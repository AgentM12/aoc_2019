package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

// Day 9 full functioning computer (to prevent code dupe)
public class IntCodeComputer {
	
	private long[] programCache;
	
	public IntCodeComputer(Input input) {
		input.setDelim(",");
		List<Long> longs = new ArrayList<>();
		while (input.hasNext()) {
			longs.add(input.nextLong());
		}
		this.programCache = longs.stream().mapToLong(i -> i).toArray();
	}
	
	public long[] getProgramCache() {
		return Arrays.copyOf(programCache, programCache.length);
	}
	
	private static class MetaData {
		int ip;
		long rb;
		Data data;
		List<Long> inputStream;
		
		public MetaData(int ip, long rb, Data data, List<Long> inputStream) {
			this.ip = ip;
			this.rb = rb;
			this.data = data;
			this.inputStream = inputStream;
		}
	}
	
	public static class Data {
		private long[] data;
		private Map<Integer, Long> otherMemory;
		
		public Data(long[] data) {
			this.data = data;
			otherMemory = new HashMap<>();
		}
		
		public void insert(int pos, long data) {
			if (pos < this.data.length)
				this.data[pos] = data;
			else
				otherMemory.put(pos, data);
		}
		
		public long fetch(int pos) {
			if (pos < data.length)
				return data[pos];
			Long r = otherMemory.get(pos);
			return (r == null ? 0 : r);
		}
	}
	
	public static class InstructionSet {
		
		public static final Map<Integer, BiFunction<Long[], MetaData, Long>> instructions;
		static {
			instructions = new HashMap<>();
			
			instructions.put(1, (params, metadata) -> {
				metadata.data.insert(params[2].intValue(), params[0] + params[1]);
				return null;
			});
			
			instructions.put(2, (params, metadata) -> {
				metadata.data.insert(params[2].intValue(), params[0] * params[1]);
				return null;
			});
			
			instructions.put(3, (params, metadata) -> {
				metadata.data.insert(params[0].intValue(), metadata.inputStream.get(0));
				metadata.inputStream.remove(0);
				return null;
			});
			
			instructions.put(4, (params, metadata) -> params[0]);
			
			instructions.put(5, (params, metadata) -> {
				if (params[0] != 0)
					metadata.ip = params[1].intValue();
				return null;
			});
			
			instructions.put(6, (params, metadata) -> {
				if (params[0] == 0)
					metadata.ip = params[1].intValue();
				return null;
			});
			
			instructions.put(7, (params, metadata) -> {
				if (params[0] < params[1])
					metadata.data.insert(params[2].intValue(), 1);
				else
					metadata.data.insert(params[2].intValue(), 0);
				return null;
			});
			
			instructions.put(8, (params, metadata) -> {
				if (params[0].equals(params[1]))
					metadata.data.insert(params[2].intValue(), 1);
				else
					metadata.data.insert(params[2].intValue(), 0);
				return null;
			});
			
			instructions.put(9, (params, metadata) -> {
				metadata.rb += params[0];
				return null;
			});
		}
		
		public static Long[] decode(Data data, int opcode, long[] params, long[] modes, long rb) {
			Long[] decodedParams = new Long[params.length];
			if (opcode == 3) {
				decodedParams[0] = (modes[0] == 2 ? rb + params[0] : params[0]);
				return decodedParams;
			}
			
			decodedParams[0] = (modes[0] == 0 ? data.fetch((int) params[0]) : (modes[0] == 2 ? data.fetch((int) (rb + params[0])) : params[0]));
			if (opcode == 4 || opcode == 9)
				return decodedParams;
			
			decodedParams[1] = (modes[1] == 0 ? data.fetch((int) params[1]) : (modes[1] == 2 ? data.fetch((int) (rb + params[1])) : params[1]));
			if (opcode == 5 || opcode == 6)
				return decodedParams;
			
			decodedParams[2] = (modes[2] == 2 ? rb + params[2] : params[2]);
			return decodedParams;
		}
		
		public static int fetchParamSize(int o) {
			if (o == 1 || o == 2 || o == 7 || o == 8)
				return 4;
			return (o == 3 || o == 4 || o == 9 ? 2 : 3);
		}
	}
	
	public static class Program {
		
		public static final int[] TP = { 1, 10, 100, 1000, 10000, 100000 };
		
		private Data data;
		private int ip;
		private long rb;
		private boolean terminated;
		
		public Program(long[] data) {
			this.data = new Data(data);
			this.ip = 0;
			this.rb = 0;
			this.terminated = false;
		}
		
		public Long nextOutput(List<Long> inputStream) {
			if (isTerminated()) {
				System.out.println("Can't run program because it has terminated.");
				return null;
			}
			while (!this.terminated) {
				int opcode = (int) (data.fetch(ip) % 100);
				long modes = data.fetch(ip) / 100;
				if (opcode == 99) {
					this.terminated = true;
					return null;
				}
				int ss = InstructionSet.fetchParamSize(opcode);
				long[] paramModes = new long[ss - 1];
				long[] params = new long[ss - 1];
				for (int i = 0; i < ss - 1; i++) {
					paramModes[i] = (modes / TP[i]) % 10;
					params[i] = data.fetch(ip + 1 + i);
				}
				BiFunction<Long[], MetaData, Long> operation = InstructionSet.instructions.get(opcode);
				if (operation == null)
					throw new IllegalArgumentException("Opcode " + opcode + " is not defined!");
				
				Long[] decodedParams = InstructionSet.decode(data, opcode, params, paramModes, rb);
				MetaData md = new MetaData(ip, rb, data, inputStream);
				Long o = operation.apply(decodedParams, md);
				rb = md.rb;
				ip = md.ip == ip ? ip + ss : md.ip;
				if (o != null)
					return o;
			}
			return null;
		}
		
		public List<Long> run(List<Long> inputStream) {
			List<Long> outputStream = new ArrayList<>();
			while (!isTerminated()) {
				Long o = nextOutput(inputStream);
				if (o != null)
					outputStream.add(o);
			}
			return outputStream;
		}
		
		public boolean isTerminated() {
			return terminated;
		}
		
	}
}
