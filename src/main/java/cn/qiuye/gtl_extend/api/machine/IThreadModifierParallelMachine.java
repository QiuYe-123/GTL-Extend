package cn.qiuye.gtl_extend.api.machine;

import cn.qiuye.gtl_extend.api.machine.multiblock.IDurationMachine;
import cn.qiuye.gtl_extend.api.machine.multiblock.IExParallelMachine;
import cn.qiuye.gtl_extend.api.machine.multiblock.IThreadModifierMachine;

public interface IThreadModifierParallelMachine extends IThreadModifierMachine, IExParallelMachine, IDurationMachine {}
