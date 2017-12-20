# cleanup-case

-----

**Currently being rewritten** --- when rewrite is complete, it'll be a lot more sensible: will not just crash on bad input, and will use a much more sensible strategy (parsing a html file saved from westlaw interface rather than using OSX-dependent utilities to convert a rtf and then regexing it).  For now, you can try the current release, but I've had reports that it's stopped working.

-----


Clean up a case downloaded from Westlaw, getting rid of all the junk their downloads included (and the copyrighted stuff to boot), leaving you with just citation, opinion, and footnotes. 

The point being to easily be able to edit and distribute cases (i.e. to students) without having to manually clean up garbage. 

I can't guarantee that this also keeps one out of heat from Westlaw for distributing publicly. My idea is also to eliminate anything West could have a copyright in, so that what's left is distributable public domain, but I wouldn't be surprised if their terms of service forbid using a to do bulk extraction and distribution.  So don't do that, or if you do that, it's at your own risk.  I intend this to be used only by professors to extract a handful of cases for easy editing to give to the students in one's own classes.

Mac only.

Requires a java runtime environment, probably recent-ish.  If this doesn't work, [here are good instructions for installing java](http://stackoverflow.com/questions/24342886/how-to-install-java-8-on-mac).

## Instructions

1.  Download the executable file from releases tab here.  Set the execute bit (`chmod +x cleancase`)

2.  Download the case(s) from westlaw in rtf format.  Relevant settings: full text, footnotes at end, don't "append" or "include" anything.

3.  Run the program on your file, being sure to escape spaces with backslashes, i.e., `./cleancase Smith\ v \Jones.rtf` 

4. Alternatively, if you have a bunch of rtf files in the same directory to convert, download process-rtfs.sh from here, rename as appropriate, set the execute bit, and use that to convert an entire directory.

Your directory will now contain a text file (or a bunch of text files) with the same names, but with .txt instead of .rtf --- and the garbage will be gone.

(c) 2017 Paul Gowder, MIT license.
