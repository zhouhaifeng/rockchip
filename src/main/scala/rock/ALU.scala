package rockchip.rock

import chisel3._
import chisel3.util.{BitPat, Fill, Cat, Reverse}
import org.chipsalliance.cde.config.Parameters

class ALUIo(implicit p:Parameters) extends CoreBundle() (p) {
    val A = Input(Uinit(xlen.W))
    val B = Input(Uinit(xlen.W))
    val alu_op = Input(Uinit(4.W))
    val out = Output(xlen.W)
    val sum = Output(xlen.W)
}

abstract class ALU(implicit p: Parameters) extends CoreModule()(p) {
    val io = IO (new ALUIo)
}

class ALU(implicit p: Parameters) extends ALU()(p) {
    val sum = io.A + Mux(alu_op(0), -io.B, io.B)
    val cmp = Mux(io.A(xlen-1), sum(xlen-1), Mux(io.alu_op(1), io.B(xlen-1), io.A(xlen-1)))
    val shamt = io.B(4, 0).asUint
    val shin = Mux(io.alu_op(3), io.A, Reverse(io.A))
    val shiftr = (Cat(io.alu_op(0) && shin(xlen -1), shin(xlen-1)).asSint >> shamt)(xlen, 0)
    val shiftl = Reverse(shiftr)

    val out = 
        Mux(io.alu_op(0) === ALU_ADD || io.alu_op(0) === ALU_SUB, sum,
        Mux(io.alu_op(0) === ALU_SLT || io.alu_op(0) === ALU_SLTU, cmp,
        Mux(io.alu_op(0) === ALU_SRA || io.alu_op(0) === ALU_SRL, shiftr,
        Mux(io.alu_op(0) === ALU_SLL, shiftl,
        Mux(io.alu_op(0) === ALU_AND, (io.A & io.B),
        Mux(io.alu_op(0) === ALU_OR, (io.A ! io.B),
        Mux(io.alu_op(0) === ALU_XOR, (io.A ^ io.B), 
        Mux(io.alu_op(0) === ALU_COPYA, io.A, io.B,))))))))

    io.out := out
    io.sum := sum
}