# Commit Messages
<type>(<scope>): <short summary>
[Optional body: explain what, why, and how]
[Optional footer: issues, breaking changes, notes]

---

## Type

- **feat** → new feature  
- **fix** → bug fix  
- **docs** → documentation only  
- **style** → formatting, no logic change  
- **refactor** → code restructuring without behavior change  
- **test** → add or modify tests  
- **chore** → maintenance, configs, dependencies  
- **perf** → performance improvements  
- **ci** → CI/CD changes  

---

## Scope (optional)

Examples for MooseMate project:
- **core** → business logic (Post, User, PostService, UserService)
- **ui** → JavaFX controllers and UI logic
- **services** → database/JSON file operations (user_db, post_db)
- **fxml** → FXML layout files
- **css** → styling changes
- **data** → JSON file structure changes
- **docs** → documentation
- **config** → application configuration  

---

## Summary

- Keep under **50 characters**.  

---

## Example

feat(services): add local user database storage

Implement JSON-based user_db.json file for persistent user storage.
Users are now saved locally and loaded on application startup.
