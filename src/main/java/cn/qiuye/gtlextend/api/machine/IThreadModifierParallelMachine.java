package cn.qiuye.gtlextend.api.machine;

import cn.qiuye.gtlextend.api.machine.multiblock.IDurationMachine;
import cn.qiuye.gtlextend.api.machine.multiblock.IExParallelMachine;
import cn.qiuye.gtlextend.api.machine.multiblock.IThreadModifierMachine;

public interface IThreadModifierParallelMachine extends IThreadModifierMachine, IExParallelMachine, IDurationMachine {}
