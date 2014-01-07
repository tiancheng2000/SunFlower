
          [ 自社制程序行统计工具-SunFlower ]


-----------------------------------------------------------------------------------------------------
                  1.10-beta 公开测试

　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　事業推進部／環境・ツールWG

新增：
- 对应C#新导入的特殊符号:'@'(使得字符串中的转义符'\'无效)
  ◆ 之前的统计工具应该都没有对应这一符号，将导致e.g.temp = @"\"; 这样的代码不能被正确解析，
     报"引号不匹配"的错误警告，并且可能错误统计注释行(少计算)。
     （因为 @"\"; 中的第二个引号、将被错误解析成被转义掉的引号，而不被视作代表字符串终了的引号符号）

- 导入ProjectType的概念，取代原本只有一层Language的概念。
  原因: 目前的language的概念、其实与projectType有混淆，
        同一language中其实混合了多种language(e.g.ASP.net中有.cs、.html、.config 3种不同格式的语言) 


  报错方式：

/*

  Subject::= SunFlower 1.00-alpha 公测BugReport

  To::= tchu@psh.com.cn; xchen@psh.com.cn

*/

-----------------------------------------------------------------------------------------------------
                  1.00-alpha 公开测试

　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　事業推進部／環境・ツールWG

新增：
- 改造开发时的 版本间比较统计机能
- C#的语言定义(configSunflowerType.xml中)

《Java+Eclipse主干课程系列》的第5回、即以其中的部分算法作为最后一次的实践练习(使用xxx类)。

[-- ToDo List --]

  参见AboutBox(新增1条)。

[-- 1.00-alpha 公测 --]

  使用帮助：

//在界面中新增"base dir"CheckBox，点选即知；或直接点击base dir的"dir.."按钮。

//操作界面简单。

//测试例：
//e.g. 准确性 - testdata下的dir1、dir2文件；可看见比较统计的结果

  报错方式：

/*

  Subject::= SunFlower 1.00-alpha 公测BugReport

  To::= tchu@psh.com.cn; xchen@psh.com.cn

*/



-----------------------------------------------------------------------------------------------------
                  0.70-alpha 公开测试

　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　事業推進部／環境・ツールWG

经历几次转手而完成的社内辅助工具(均因兼职开发而无疾而终)。

本次设计，使得能通过修改Config XML文件 来处理未来新语种；

《本質的な設計の採掘》中所引用的就是本例的SD —— 要素分析 + 状态图。

本程序系由Java编写的 SWT GUI界面，即编制Eclipse所用的GUI方式(速度快、但swt.jar包需1M*)。

经由Windows Launcher启动。

* 改写MANIFEST.MF中的classpath可与其它SWT运用共享同一jar包。


[-- ToDo List --]

  参见AboutBox。


[-- 0.70-alpha 公测 --]

  —— 各阶段[测试]的本质，是以[更低]的Cost、来复查以其前阶段的方式难查的(那类)不周之处 的一种手段。
    
     (管理/策略设计也与SD/DD/MK/MKR/..类似、合适的地方通过[试运行]+[收集/分析反馈] 超阶段进行)
    
                                                                -- Timothy

  使用帮助：

//configSunflowerType.xml 中含修改Config XML文件的帮助；

//操作界面简单。

//测试例：
//e.g. 准确性 - testdata下的_MainTest.java文件；可比比看SourceCounting与SunFlower的结果
//e.g. 便利性 - Config XML自定义；可指定单个文件；指定目录、并将后缀名设成空(或*.*)后，
//              统计时能自动选择合适的编程语言种别

  报错方式：

/*

  Subject::= SunFlower 0.70-alpha 公测BugReport

  To::= tchu@psh.com.cn; xchen@psh.com.cn

*/

