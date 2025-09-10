COMMIT MESSAGES -->
<type>(<scope>): <short summary>
[Optional body: explain what, why, and how]
[Optional footer: issues, breaking changes, notes]

  Type:
feat → new feature
fix → bug fix
docs → documentation only
style → formatting, no logic change
refactor → code restructuring without behavior change
test → add or modify tests
chore → maintenance, configs, dependencies
perf → performance improvements
ci → CI/CD changes

  Scope (optional):
Examples: auth, api, ui, docs, deps.

  Summary:
Under 50 characters

  Example:
feat(auth): add JWT-based login

Implement JWT authentication for the login system to improve security
and allow stateless session handling. This replaces the old cookie-
based mechanism.

Closes #42
BREAKING CHANGE: existing sessions will be invalid after deployment.
