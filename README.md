# Tomasulo算法模拟器实验文档

小组成员

2014011333 计43 丁铭 

2014011335 计43 王红梅

## 算法思路
Tomasulo算法通过寄存器换名来消除WAW和WAR冲突，从而允许指令的乱序执行。其中寄存器换名也可理解为用由保留站、load/store Buffer组成的虚拟内存集代替真实的浮点寄存器。
在该算法中，指令的执行分为以下三步：

(1) Issue
从浮点操作队列中取出指令，如果保留站有空闲，发射指令到保留站；
如果操作数已就绪，将操作数也送入保留站，如果操作数未就绪，将产生操作数的保留站标识送入保留站。

(2) Execution
在操作数都就绪后调用功能部件进行计算，如果所需操作数未就绪，等待产生操作数的保留站产生结果。

(3) write back
计算结束后将计算结果写入到所有等待该结果的寄存器中和保留站中，并将当前保留站标记为可用

## 主要接口与类
###### ReservationStation
保留站的基类，定义了保留站名称name、轮数time、空闲状态isBusy和执行指令command等成员变量。
###### LoadReservationStation
load缓存站类，继承ReservationStation类，增加地址计算Addr成员变量。
###### StoreReservationStation
store缓存站类，继承ReservationStation类，增加地址计算Addr，缓存站qj和数值vj等成员变量。
###### CalcReservationStation
Calculate保留站类，继承ReservationStation类，增加指令操作的类型Op，第一个源操作数Vj，第二个源操作数Vk，产生第一个源操作数的保留站名称qj，产生第二个源操作数的保留站名称qk等成员变量。
###### Command
指令信息类，定义指令操作类型op，操作数及寄存器参数数组arg，执行状态值issue、comp和result，以及指令执行结果value。另外定义成员函数toString()，用String数组存储指令所有信息，方便指令信息的界面显示。
###### FunctionUnit
FU寄存器类，定义寄存器名称name，寄存器中的值v和寄存器中保留站r。
###### Helper
定义函数readCommandsFromFile和readCommandsFromFileByDialog，实现从文件对话框选定的文件中读取指令。
###### LogicInterface
该接口定义了逻辑计算过程中需要用到的以下方法：

```
public FunctionUnit[] getFunctionUnits();
public int getClock();
public ReservationStation[] getReservationStations(String op);
public ArrayList<Command> getCommands();
public int[] getRegisters();
public double[] getMemory();
public void clear();
public boolean runCycle();
public int getPc();
```
###### Logic
logic类主要实现LogicInterface中定义的方法，完成逻辑运算的实现。
首先通过构造函数完成3个Add保留站、2个Mult保留站、3个load缓冲站和3个store缓冲站以及FU列表，RU列表，MEM的创建与初始化。
然后是对LogicInterface中方法的实现，其中比较核心的是runcycle()方法，该方法通过调用countdown函数根据指令剩余执行时间time对处于保留站的指令的执行状态进行修改，并计算结果。然后对保留站进行检查，对处于等待状态的保留站，将产生所等操作数的保留站中的值写入对应操作数。若还有指令需要执行且保留站有空闲，发射指令，根据指令类型修改保留站的信息。

###### UserWindow
用户界面类，在界面中显示：
操作工具栏（包括从文件读指令，编辑指令/寄存器内容，完成编辑，单步执行，自动执行，停止自动执行，重置模拟器等操作）
数据表格区（包括指令内容与执行状态，load缓存站内容，store缓存站内容，保留站内容，FU寄存器内容，RU寄存器内容，MEM部分内容显示）

该类中实现工具栏中操作对应功能的函数如下：
loadCommands()：通过dialog选择文件读取指令，调用updateTable显示指令内容
openEditableMode()：令指令Table、RU寄存器Table以及MemoryTable处于可编辑状态
closeEditableMode()：关闭可编辑状态，读取编辑的内容到logic中
runOneCycle()：调用logic中的runCycle()执行一步，再调用updateTable()更新表格内容
autoRun()：设置最大执行轮数，启动自动执行，在计时器时间内调用runOneCycle()
stopAutoRun()：停止自动执行，关闭计时器。
reset()：恢复最原始的数据信息，更新表格
writeTable()：用于将寄存器表、指令表格和mem中的数据读到logic中
updateTable()：从logic中读取所有表格信息，进行数据更新
displayMemory(addr)：显示从起始位置addr往后n位的mem信息
###### UniversalActionListener
全局监听类，实现ActionListener接口定义的方法ActionPerformed()，根据action的来源做出相应处理，调用UserWindow的相应方法。

## 实现的功能

根据实验要求，模拟器实现了如下功能：

* 模拟器能够执行浮点加、减、乘、除运算及 LOAD 和 STORE操作

* 执行指令过程中实时显示算法的运行状况，包括各条指令的运行状态、各寄存器以及内存的值、保留站状态、Load Buffer和Store Buffer状态、指令执行周期数

* 模拟器支持单步执行和多步执行，多步自动执行可使用户设置最大执行周期数和单周期时间，且提供停止自动执行功能

* 支持从文件中加载命令和通过界面编辑命令两种方式

 编辑模式下，界面中命令、内存、整数寄存器相关的表格将变为可编辑状态，编辑完成后可通过点击“确定”生效。

* 重置模拟器状态

* 用户界面如下所示：


## 实验结果
