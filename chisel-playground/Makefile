BUILD_DIR = ./build
VIVADO_DIR = /home/cao/tcpudp2mac/tcpudp2mac-vivado

PRJ = playground

test:
	mill -i $(PRJ).test

verilog:
	$(call git_commit, "generate verilog")
	mkdir -p $(BUILD_DIR)
	mill -i $(PRJ).runMain Elaborate --target-dir $(BUILD_DIR)

vivado:
	rm -rf $(BUILD_DIR)/*
	rm -rf $(VIVADO_DIR)/sv/*
	mill -i $(PRJ).runMain Elaborate --target-dir $(BUILD_DIR)
	cp $(BUILD_DIR)/*.sv $(VIVADO_DIR)/sv/

help:
	mill -i $(PRJ).runMain Elaborate --help

reformat:
	mill -i __.reformat

checkformat:
	mill -i __.checkFormat

bsp:
	mill -i mill.bsp.BSP/install

idea:
	mill -i mill.idea.GenIdea/idea

clean:
	-rm -rf $(BUILD_DIR)

.PHONY: test verilog help reformat checkformat clean

sim:
	$(call git_commit, "sim RTL") # DO NOT REMOVE THIS LINE!!!
	@echo "Write this Makefile by yourself."

-include ../Makefile
