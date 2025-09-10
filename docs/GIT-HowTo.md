# Team Git & Commit Guide

Denne filen beskriver hvordan vi jobber med Git i prosjektet, inkludert commit-meldinger, branch-struktur, og vanlige kommandoer.

---

## 1. Commit Messages

<type>(<scope>): <short summary>  
[Optional body: explain what, why, and how]  
[Optional footer: issues, breaking changes, notes]

### Type
- feat → new feature  
- fix → bug fix  
- docs → documentation only  
- style → formatting, no logic change  
- refactor → code restructuring without behavior change  
- test → add or modify tests  
- chore → maintenance, configs, dependencies  
- perf → performance improvements  
- ci → CI/CD changes  

### Scope (optional)
Eksempler: auth, api, ui, docs, deps.  

### Summary
- Hold under 50 tegn  
- Bruk imperativ form: “Add login button” ikke “Added login button”

### Eksempel
feat(auth): add JWT-based login

Implement JWT authentication for the login system to improve security
and allow stateless session handling. Dette erstatter den gamle cookie-
baserte mekanismen.

Closes #42  
BREAKING CHANGE: eksisterende sesjoner vil bli ugyldige etter deploy.

---

## 2. Git Workflow

### 2.1 Klon repoet (første gang)
git clone <repo-url>  
cd <mappenavn>

### 2.2 Oppdater fra main
git checkout main  
git pull origin main

### 2.3 Opprett en ny branch
git checkout -b feature/login-page  

**Branch-konvensjon:**
- feature/... → nye funksjoner  
- fix/... → feilrettinger  
- docs/... → dokumentasjon  
- chore/... → vedlikehold, config, dependencies  

### 2.4 Gjør endringer og legg til filer
git add .  
# eller  
git add <filnavn>

### 2.5 Commit endringer
git commit -m "feat(auth): add JWT-based login"

### 2.6 Push branchen
git push origin feature/login-page

### 2.7 Lag en Pull Request (PR)
- Opprett PR fra din branch til main  
- Tydelig tittel og beskrivelse  
- Lenke til issues hvis relevant  

### 2.8 Oppdater branchen med main
git checkout main  
git pull origin main  
git checkout feature/login-page  
git merge main

### 2.9 Slett branch etter merge
git branch -d feature/login-page  
git push origin --delete feature/login-page

---

## 3. Tips for godt Git-arbeid
- Små og hyppige commits  
- Meningsfulle commit-meldinger  
- Oppdater branchen ofte for å unngå konflikter  
- Ikke push direkte til main
