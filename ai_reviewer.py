import requests
import os
import sys

GEMINI_API_KEY = os.getenv("GEMINI_API_KEY")
GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")
REPO = "chaggyneutron/puissance4"

def get_pr_diff(pr_number):
    url = f"https://api.github.com/repos/{REPO}/pulls/{pr_number}"
    headers = {
        "Authorization": f"token {GITHUB_TOKEN}",
        "Accept": "application/vnd.github.v3.diff"
    }
    response = requests.get(url, headers=headers)
    return response.text

def review_with_gemini(diff):
    prompt = f"""You are a senior code reviewer. Analyze this git diff from a Spring Boot project and provide:

1. **Security issues** — hardcoded secrets, vulnerabilities, auth problems
2. **Bugs** — logic errors, null pointers, edge cases
3. **Bad practices** — code quality, naming, structure
4. **Summary** — 2-3 sentences on what this PR does

Keep it concise and actionable. If the diff looks good, say so.

Git diff:
{diff}
"""
    response = requests.post(
        f"https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key={GEMINI_API_KEY}",
        headers={"Content-Type": "application/json"},
        json={"contents": [{"parts": [{"text": prompt}]}]}
    )
    data = response.json()
    if "error" in data:
        return f"Gemini error: {data['error']['message']}"
    return data["candidates"][0]["content"]["parts"][0]["text"]

def post_pr_comment(pr_number, comment):
    url = f"https://api.github.com/repos/{REPO}/issues/{pr_number}/comments"
    headers = {
        "Authorization": f"token {GITHUB_TOKEN}",
        "Content-Type": "application/json"
    }
    body = f"## 🤖 AI Code Review\n\n{comment}\n\n---\n*Powered by Gemini 3.5 Flash*"
    response = requests.post(url, headers=headers, json={"body": body})
    return response.status_code == 201

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python3 ai_reviewer.py <pr_number>")
        sys.exit(1)

    pr_number = sys.argv[1]
    print(f"Fetching diff for PR #{pr_number}...")
    diff = get_pr_diff(pr_number)

    if not diff:
        print("No diff found.")
        sys.exit(1)

    print("Sending to Gemini for review...")
    review = review_with_gemini(diff)

    print("Posting comment on PR...")
    success = post_pr_comment(pr_number, review)

    if success:
        print(f"✅ Review posted on PR #{pr_number}")
    else:
        print("❌ Failed to post comment")
    
    print("\n--- Review ---")
    print(review)
