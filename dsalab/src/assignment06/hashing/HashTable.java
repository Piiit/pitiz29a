package assignment06.hashing;

public class HashTable {

	private final static int DEFAULT_CAPACITY = 16384; //2^14
	private Word[] table;
	private int capacity;
	private int countUnique;
	private int countAll;
	private int countCollisions;

	public HashTable(int capacity) {
		this.table = new Word[capacity];
		this.capacity = capacity;
		this.countUnique = 0;
		this.countAll = 0;
		this.countCollisions = 0;
	}
	
	public HashTable() {
		this(DEFAULT_CAPACITY);
	}

	public int getCapacity() {
		return capacity;
	}

	public int getCount() {
		return countUnique;
	}
	
	public int getCountAll() {
		return countAll;
	}
	
	public int getCountCollisions() {
		return countCollisions;
	}

	public void insert(int hashCode, String word) {
		if (word == null) {
			throw new NullPointerException();
		}
		
		//Make sure that the index isn't out of bounds. 
		int index = hashCode % capacity;
		if(index < 0) {
			index = index * (-1);
		}
		
		if(table[index] == null) {
			table[index] = new Word(word);
			countUnique++;
			countAll++;
			System.out.format("Inserting word '%s' with hash '%d' at index %d.\n", word, word.hashCode(), index);
		} else {
			countAll++;
			Word current = table[index];
			int i = 0;
			while(current.chain != null && !current.value.equalsIgnoreCase(word)) {
				current = current.chain;
				i++;
			}
			if(current.value.equalsIgnoreCase(word)) {
				System.out.format("Inserting word '%s' with hash '%d' at index %d failed! Word already exists.\n", word, word.hashCode(), index, i);
			} else {
				countCollisions++;
				current.chain = new Word(word);
				countUnique++;
				System.out.format("Inserting word '%s' with hash '%d' at index %d, chained at %d.\n", word, word.hashCode(), index, i);
			}
		}
	}
	
	@Override
	public String toString() {
		float p = (float)countUnique/countAll * 100;
		float p2 = (float)countCollisions/countUnique * 100;
		return "HashTable [capacity=" + capacity + 
				", countUnique=" + countUnique + 
				", countAll=" + countAll + 
				", countCollisions=" + countCollisions + 
				", percentage of different words=" + p + 
				", percentage of collisions=" + p2 +
				"]";
	}


	
}
