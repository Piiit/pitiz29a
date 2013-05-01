package assignment05;

public class QuickSort {

//	QuickSort(Links,Rechts);
//	 //Links, Rechts sind hier Zeiger
//	  falls Links<>Rechts dann
//	     Ptr1:=Links
//	     Ptr2:=Links
//	     Ptr0:=Links
//	     //Initialisierung der (lokalen) Zeiger; Ptr0 wird nur als Vorgänger von Ptr1 benötigt
//	     Pivot:=Links.Zahl
//	     wiederhole
//	        Ptr2:=Ptr2.Nachfolger;
//	        falls Ptr2.Zahl<Pivot dann
//	           Ptr0:=Ptr1; 
//	           Ptr1:=Ptr1.Nachfolger;
//	           tausche(Ptr1,Ptr2)
//	     solange bis Ptr2=Rechts;
//	     tausche(Links,Ptr1)
//	     falls Ptr1<>Rechts dann Ptr1:=Ptr1.Nachfolger;
//	     QuickSort(Links,Ptr0);
//	     QuickSort(Ptr1,Rechts)
//	 ende
	
	public static void sort(HTDLList list) {
		QuickSort(list.root, list.tail);
	}
	
	
	
	private static void QuickSort(DLLNode left, DLLNode right) {
		if (left != right) {
			DLLNode pointer0 = left;
			DLLNode pointer1 = left;
			DLLNode pointer2 = left;
			
			int pivot = left.value;
			
			do {
				pointer2 = pointer2.suc;
				if(pointer2.value < pivot) {
					pointer0 = pointer1;
					pointer1 = pointer1.suc;
					exchange(pointer1, pointer2);
				}
			} while (pointer2 != right);
			exchange(left, pointer1);
			if(pointer1 != right) {
				pointer1 = pointer1.suc;
			}
			QuickSort(left, pointer0);
			QuickSort(pointer1, right);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
