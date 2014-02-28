SunFlower -- 一个可扩展的行数统计工具
=====================================

功能
------
* Most of all, users can deal with any NEW languages by just editing a config file (configSunflowerType.xml).
* Java SWT based application, so theoretically compatible to \*nix platforms (need libswt-\*.so and path setting).
* Support encoding including UTF8, Unicode, ISO8859_1, Shift-JIS(Japanese).
* Report results by File, Folder, Total Lines, Source Lines, Comment Lines, Blank Lines, and Language
* Differentiated source counting between 2 project folders (check on "base dir"). 

History
-------
This tool was a part time company assignment assigned and accomplished by a team of 2, in 2006.  
Before that, other groups tried but gave up by different reasons.  
After that, however, dueing to unknown reasons, the tool has not been popularized in the company.  
Nowadays, there is a lot of choice in this field.  
Of course, as the designer and one of the developer of SunFlower, i prefer using it personally.  

Valuable things
---------------
* For those who're interested in LOC, complete set of source and a state chart used to analyse  
  the source parsing logic are made public.
* SWT is compatible to *nix platforms. Developers on *nix may tune libswt-*.so and path setting,  
  and shared it back to the community. There are some trivial bugs, as well~
* Revise this tool to a [ThinkAlike][]-based Java MVVM application?  
  (SWT is compatible with ThinkAlike. However, Counting source on mobile device is not a common use case :))  
  [ThinkAlike]: https://github.com/tiancheng2000/ThinkAlike

Thanks to 
---------
* Xiao Chen, another team member of developing this tool application.  
  Remember those afternoons discussing the state chart of source code analyse (much simpler  
  than Compiling Theory learned in university though).
* Sunflower, as the birthday flower of July :) 

