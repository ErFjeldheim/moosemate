# Team Git & Commit Guide

Denne filen beskriver hvordan vi jobber med Git i prosjektet, inkludert commit-meldinger, branch-struktur, og vanlige kommandoer.

---

## Git Workflow

### Oppdater fra main
git checkout main  
git pull origin main

### Opprett en ny branch
Velg issue du vil opprette branch fra i issue board
Create new branch
Behold issuenummer, kort konkret beskrivelse at issue/branch. 


### Gjør endringer og legg til filer
git add .  (adder alle nye filer) 

eller:

git add <filnavn>

eventuelt bruk implementert i vsCode

### Commit endringer
git commit -m "feat(auth): add JWT-based login"

### Push endringer på branch til samme branch
git add .
git commit -m "Din commit-melding"
git push origin <branch-navn>

om du VET du er på rett branch: 
git push

### Lag en Pull Request (PR)
- Opprett PR fra din branch til main  
- Tydelig tittel og beskrivelse  
- Lenke til issues hvis relevant
- Legg til gruppemedlemmer som reviewers

### Oppdater branchen din med nyeste main
git checkout <din-branch>  (flytter deg til din branch om du mot formdodning er på en annen) 
git fetch origin main
git merge origin/main

### Slett branch etter merge
git checkout main
git branch -d <branch-name>
git push origin --delete <branch-name> 

---

## 3. Tips for godt Git-arbeid
- Små og hyppige commits  
- Meningsfulle commit-meldinger  
- Oppdater branchen ofte for å unngå konflikter  
- Ikke push direkte til main! 
