# cleanup-case

Clean up a case downloaded from Westlaw, getting rid of all the junk their downloads included (and the copyrighted stuff to boot), leaving you with just citation, opinion, and footnotes. 

The point being to easily be able to edit and distribute cases (i.e. to students) without having to manually clean up garbage. 

I can't guarantee that this also keeps one out of heat from Westlaw for distributing publicly. My idea is also to eliminate anything West could have a copyright in, so that what's left is distributable public domain, but I wouldn't be surprised if their terms of service forbid using a to do bulk extraction and distribution.  So don't do that, or if you do that, it's at your own risk.  I intend this to be used only by professors to extract a handful of cases for easy editing to give to the students in one's own classes.

Mac only.

## Instructions

1.  Download from westlaw in rtf format.  Relevant settings: full text, footnotes at end, don't "append" or "include" anything.

2.  Use the mac utility textutil to turn the rtf files into html. e.g.  

``` 
textutil National\ League\ of\ Cities\ v\ Usery.rtf -convert html
```

3.  Run this on the resulting html file.  It'll spit out a plain text file with all the garbage removed.

MIT license.
