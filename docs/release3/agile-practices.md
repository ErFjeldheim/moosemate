# Agile Practices

This document describes the agile practices and methodologies used throughout the MooseMate project for IT1901 at NTNU.

## Methodology

We use **Scrum** as our primary agile framework, with sprints lasting 1-2 weeks. This approach allows us to maintain flexibility, adapt to changing requirements, and deliver incremental value throughout the project.

## Sprint Structure

### Sprint Planning

- Held on Thursday mornings at the start of each sprint
- Review and prioritize issues from the Git project board
- Break down larger requirements into smaller, manageable issues
- Assign issues to team members or pairs
- Estimate effort and set sprint goals

### Daily Standups

- Conducted at the beginning of each work session
- Each team member summarizes:
  - What was completed since the last meeting
  - Potential blockers or challenges
  - Planned work for the upcoming sprint/session
- Keeps the team aligned and identifies issues early

### Sprint Retrospectives

- Held on Wednesdays at the end of each sprint
- Reflect on what went well and what could be improved
- Discuss process adjustments for the next sprint
- Meeting day can be adjusted by group consensus (e.g., due to illness, travel)

## Roles and Responsibilities

| Role | Responsibility | Member |
| --- | --- | --- |
| Project Manager | Meeting planning, progress tracking, delivery coordination | Katharina |
| Branch Manager | Git branch management, naming conventions, organization | Syver |
| Documentation | Maintaining up-to-date documentation in lightweight format | All members |

## Issue Management

- **Tool**: GitHub Issues with project board views
- **Workflow**:
  - Issues created with checkbox-based requirements
  - When all checkboxes are complete, a Pull Request is created
  - Issues organized into release-specific views for progress tracking
  - Requirements divided into smaller issues for better monitoring

## Code Review Process

- Pull requests require at least one reviewer from the group
- Reviewers test the branch locally
- Code review focuses on:
  - Bug detection
  - Code duplication
  - General code quality
- Pair programming serves as a form of real-time peer review

## Development Practices

### Pair Programming

- Primary development practice for larger features
- Benefits:
  - Both participants learn more and gain deeper insight
  - Higher code quality with fewer bugs
  - Multiple team members familiar with the same code areas
  - Resilience when team members are absent
- Some tasks better suited for individual work:
  - Documentation updates
  - Proofreading and correcting existing code
  - Smaller fixes requiring minimal code changes

### Version Control

- Follow [GitHowTo](../GIT-HowTo.md) and [CommitTemplate](../CommitTemplate.md) guidelines
- Medium-sized issues with corresponding branches
- Small, frequent commits to prevent code loss
- Feature branches merged via pull requests after review

### Code Quality Tools

- **Spotless**: Eclipse Java Style formatting
- **Checkstyle**: Code style enforcement
- **Spotbugs**: Bug detection
- **JaCoCo**: Test coverage reporting
- **JUnit 5**: Unit testing framework
- **TestFX**: UI testing framework

## Communication

- **Primary channels**: Messenger and Slack for daily coordination
- **Formal communication**: Email/NTNU channels for meeting minutes and official notices
- **Shared calendar**: Meeting schedules, sprint deadlines, delivery dates

## Adaptations and Lessons Learned

### Release 2

- Recognized the value of combining pair programming with individual work
- Time management and consistency allowed the team to stay ahead
- Identified that patience and structure are required for effective pair programming

### Release 3

- Started with plan to migrate frontend to React
- Refactored for one day before deciding to stick with JavaFX
- Split into two teams for backend and frontend work
- Held design workshop for forum feed layout brainstorming
- Team members rotated between documentation and fullstack work to balance workload
- Learned importance of well-planned issues for efficiency
- GitHub project board proved valuable for progress tracking and coordination

## Meeting Requirements

- **Lectures**: Minimum 75% attendance per person
- **Teaching Assistant Meetings**: Minimum 75% attendance per person
- **Internal Project Day**: Every Thursday, 09:00 - 15:00
- **Absence Policy**: Notify at least 24 hours in advance (exceptions for acute illness)
