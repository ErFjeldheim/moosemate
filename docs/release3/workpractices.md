## Work Practices

We have continued with the same practices and workflow as for previous releases. We have combined both pair programming and individual programming, as the group still only consists of odd number of people. We have tried to limit the individual work to documentation and smaller fixes, as it requires least amount of code, in comparison to for example implementation of a completely new feature (as Session ID). 

As for previous releases, the work practice has included medium sized issues, with corresponding branch. Furthermore, we have tried to keep up with make smaller commits, as soon as a new feat etc. is functioning, to limit the amount of lost code if any issue were to occur. This has saved us from loosing functioning code for one feat, if the newest implemented feat does not function. 

In the last few commits we had some issues with Eclipse Che configuration, pushing a `devfile.yaml` directly to main, because Che uses git remote repo to run. We ended up removing this file, but it resulted in some one-off case direct pushes to main.

The issues on github have been desriped with chechboxes to meet the requirements, and when these were all checked, an PR was made. A peer, or more, in the group were put as reviewers, and tried running the branch locally. The reviewer also reviewew the code itself, to look for bugs, duplications etc. This is sometimes hard to do, as the amount of code can feel overwhelming. However, as we obtained to pair programming for larger features, there were rarely any big bugs to be discovered, as the code was already peer-reviewd in a type of way. 

We still aim to follow the [GitHowTo](../GIT-HowTo.md) and [CommitTemplate](../CommitTemplate.md) for the commit structure, though some confusion between e.g. "fix" and "test", when fixing a test could be mixed up at times. 