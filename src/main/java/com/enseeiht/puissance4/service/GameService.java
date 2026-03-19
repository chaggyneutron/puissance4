package com.enseeiht.puissance4.service;

import com.enseeiht.puissance4.entity.Game;
import com.enseeiht.puissance4.entity.Move;
import com.enseeiht.puissance4.entity.User;
import com.enseeiht.puissance4.repository.GameRepository;
import com.enseeiht.puissance4.repository.MoveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final MoveRepository moveRepository;
    private final RankingService rankingService;

    public Game createGame(User player1) {
        Game game = Game.builder()
                .player1(player1)
                .currentTurn(player1)
                .build();
        return gameRepository.save(game);
    }

    @Transactional
    public Game joinGame(Long gameId, User player2) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partie non trouvée"));
        if (game.getStatus() != Game.Status.WAITING)
            throw new RuntimeException("Partie non disponible");
        if (game.getPlayer1().getId().equals(player2.getId()))
            throw new RuntimeException("Vous ne pouvez pas jouer contre vous-même");

        game.setPlayer2(player2);
        game.setStatus(Game.Status.IN_PROGRESS);
        return gameRepository.save(game);
    }

    @Transactional
    public Game playMove(Long gameId, User player, int column) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partie non trouvée"));

        if (game.getStatus() != Game.Status.IN_PROGRESS)
            throw new RuntimeException("La partie n'est pas en cours");
        if (!game.getCurrentTurn().getId().equals(player.getId()))
            throw new RuntimeException("Ce n'est pas votre tour");

        // Calcule la ligne disponible dans la colonne
        String[] rows = game.getBoard().split(",");
        int row = -1;
        for (int r = 5; r >= 0; r--) {
            if (rows[r].charAt(column) == '0') { row = r; break; }
        }
        if (row == -1) throw new RuntimeException("Colonne pleine");

        // Place le jeton
        int playerNum = game.getPlayer1().getId().equals(player.getId()) ? 1 : 2;
        char[] rowChars = rows[row].toCharArray();
        rowChars[column] = (char) ('0' + playerNum);
        rows[row] = new String(rowChars);
        game.setBoard(String.join(",", rows));

        // Sauvegarde le coup
        Move move = Move.builder()
                .game(game).player(player).column(column).row(row).build();
        moveRepository.save(move);

        // Vérifie victoire
        if (checkWin(rows, row, column, playerNum)) {
            game.setStatus(Game.Status.FINISHED);
            game.setWinner(player);
            game.setFinishedAt(LocalDateTime.now());
            rankingService.updateRanking(player,
                    playerNum == 1 ? game.getPlayer2() : game.getPlayer1());
        } else if (isBoardFull(rows)) {
            game.setStatus(Game.Status.FINISHED);
            game.setFinishedAt(LocalDateTime.now());
        } else {
            // Change de tour
            game.setCurrentTurn(
                    game.getPlayer1().getId().equals(player.getId())
                            ? game.getPlayer2() : game.getPlayer1());
        }

        return gameRepository.save(game);
    }

    public Game abandonGame(Long gameId, User player) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partie non trouvée"));
        game.setStatus(Game.Status.ABANDONED);
        game.setFinishedAt(LocalDateTime.now());
        User opponent = game.getPlayer1().getId().equals(player.getId())
                ? game.getPlayer2() : game.getPlayer1();
        game.setWinner(opponent);
        return gameRepository.save(game);
    }

    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partie non trouvée"));
    }

    public List<Game> getAvailableGames() {
        return gameRepository.findByStatus(Game.Status.WAITING);
    }

    // ── Logique Puissance 4 ───────────────────────────────────────────────────
    private boolean checkWin(String[] rows, int row, int col, int player) {
        int[][] board = parseBoard(rows);
        return checkDirection(board, row, col, player, 1, 0)  // horizontal
            || checkDirection(board, row, col, player, 0, 1)  // vertical
            || checkDirection(board, row, col, player, 1, 1)  // diagonale
            || checkDirection(board, row, col, player, 1, -1); // anti-diagonale
    }

    private boolean checkDirection(int[][] board, int row, int col,
                                   int player, int dr, int dc) {
        int count = 1;
        for (int d : new int[]{-1, 1}) {
            int r = row + d * dr, c = col + d * dc;
            while (r >= 0 && r < 6 && c >= 0 && c < 7 && board[r][c] == player) {
                count++; r += d * dr; c += d * dc;
            }
        }
        return count >= 4;
    }

    private int[][] parseBoard(String[] rows) {
        int[][] board = new int[6][7];
        for (int r = 0; r < 6; r++)
            for (int c = 0; c < 7; c++)
                board[r][c] = rows[r].charAt(c) - '0';
        return board;
    }

    private boolean isBoardFull(String[] rows) {
        for (String row : rows)
            if (row.contains("0")) return false;
        return true;
    }
}
