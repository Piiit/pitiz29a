package tools;

public interface Algorithm {
	public void execute() throws Exception;
	public String getName();
	public int[] getArray();
	public void setArray(int[] array);
	public int getElementCount();
}
