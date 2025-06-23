`timescale 1ns / 1ps

module tb_AxiAnalysis;

  // ??????
  reg clock;
  reg reset;
  reg io_in_tvalid;
  reg [31:0] io_in_tdata;
  reg [3:0] io_in_tkeep;
  reg [3:0] io_in_tstrb;
  reg io_in_tlast;
  reg [3:0] io_in_tid;
  reg [3:0] io_in_tdest;
  reg [3:0] io_in_tuser;
  reg io_out_udp_ready;
  reg io_out_tcp_ready;

  // ??????
  wire io_in_tready;
  wire io_out_udp_valid;
  wire [511:0] io_out_udp_bits_data;
  wire [15:0] io_out_udp_bits_len;
  wire [15:0] io_out_udp_bits_udp_head_src_port;
  wire [15:0] io_out_udp_bits_udp_head_dst_port;
  wire [15:0] io_out_udp_bits_udp_head_length;
  wire [15:0] io_out_udp_bits_udp_head_checksum;
  wire [15:0] io_out_tcp_valid;
  wire [511:0] io_out_tcp_bits_data;
  wire [15:0] io_out_tcp_bits_len;

  // ??? AxiAnalysis ??
  Top uut (
    .clock(clock),
    .reset(reset),
    .io_axis_tvalid(io_in_tvalid),
    .io_axis_tready(io_in_tready),
    .io_axis_tdata(io_in_tdata),
    .io_axis_tkeep(io_in_tkeep),
    .io_axis_tstrb(io_in_tstrb),
    .io_axis_tlast(io_in_tlast),
    .io_axis_tid(io_in_tid),
    .io_axis_tdest(io_in_tdest),
    .io_axis_tuser(io_in_tuser)
  );

  // ????
  always begin
    #5 clock = ~clock;  // ?????10ns
  end

  // ?? tlast ???
  reg [7:0] clk_count;  // ?????

  always @(posedge clock) begin
    if (reset) begin
      clk_count <= 0;
      io_in_tlast <= 0;  // ??? tlast ? 0
    end else begin
      clk_count <= clk_count + 1;  // ????????????
      if (clk_count == 3) begin
        io_in_tlast <= 1;  // ?? 10 ???????? tlast ? 1
      end
      else begin
        io_in_tlast <= 0;
      end
      if(clk_count == 4) begin
        io_in_tvalid <= 0;
      end
    end
  end

  // ????
  initial begin
    // ?????
    clock = 0;
    reset = 0;
    io_in_tvalid = 0;
    io_in_tdata = 32'h82345678;
    io_in_tkeep = 4'b1101;
    io_in_tstrb = 4'b1001;
    io_in_tlast = 0;
    io_in_tid = 4'b0001;
    io_in_tdest = 4'b0010;
    io_in_tuser = 4'b0011;
    io_out_udp_ready = 0;
    io_out_tcp_ready = 0;

    // ??
    reset = 1;
    #10;
    reset = 0;

    // ??????
    io_in_tvalid = 1;
    io_out_udp_ready = 1;  // ?? UDP ?????
    io_out_tcp_ready = 1;  // ?? TCP ?????

    // ????????????
    #100;

    // ????
    $finish;
  end

endmodule
