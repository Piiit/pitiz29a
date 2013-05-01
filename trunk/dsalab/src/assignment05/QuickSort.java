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
	
	public static void sort(HTList list) {
		quickSort(list.head, list.tail);
	}
	
	
	
	private static void quickSort(Node left, Node right) {
		if (left != right) {
			Node pointer1_pre = left;
			Node pointer1 = left;
			Node pointer2 = left;
			
			int pivot = left.value;
			
			do {
				pointer2 = pointer2.suc;
				if(pointer2.value < pivot) {
					pointer1_pre = pointer1;
					pointer1 = pointer1.suc;
					HTList.exchangeValues(pointer1, pointer2);
				}
			} while (pointer2 != right);
			HTList.exchangeValues(left, pointer1);
			if(pointer1 != right) {
				pointer1 = pointer1.suc;
			}
			quickSort(left, pointer1_pre);
			quickSort(pointer1, right);
		}
	}
	
}
