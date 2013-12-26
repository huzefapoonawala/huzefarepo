package com.simplydifferent.util;

public class Num2WordConvertor {

	private static Num2WordConvertor singleton;
	
	public synchronized static Num2WordConvertor getInstance() {
		if (singleton == null) {
			singleton = new Num2WordConvertor();
		}
		return singleton;
	}
	
	String string;
	String a[]={"",
				"one",
				"two",
				"three",
				"four",
				"five",
				"six",
				"seven",
				"eight",
				"nine",
			};

	String b[]={
				"hundred",
				"thousand",
				"lakh",
				"crore"
			};

	String c[]={"ten",
				"eleven",
				"twelve",
				"thirteen",
				"fourteen",
				"fifteen",
				"sixteen",
				"seventeen",
				"eighteen",
				"ninteen",
			};

	String d[]={"twenty",
				"thirty",
				"fourty",
				"fifty",
				"sixty",
				"seventy",
				"eighty",
				"ninty"
			};



	public String convertNumToWord(int number){

		int c=1;
		int rm ;
		string="";
		while ( number != 0 )
		{
			switch ( c )
			{
				case 1 :
					rm = number % 100 ;
					pass ( rm ) ;
					if( number > 100 && number % 100 != 0 )
					{
						display ( "and " ) ;
					}
					number /= 100 ;

					break ;

				case 2 :
					rm = number % 10 ;
					if ( rm != 0 )
					{
						display ( " " ) ;
						display ( b[0] ) ;
						display ( " " ) ;
						pass ( rm ) ;
					}
					number /= 10 ;
					break ;

				case 3 :
					rm = number % 100 ;
					if ( rm != 0 )
					{
						display ( " " ) ;
						display ( b[1] ) ;
						display ( " " ) ;
						pass ( rm ) ;
					}
					number /= 100 ;
					break ;
					
				case 4 :
					rm = number % 100 ;
					if ( rm != 0 )
					{
						display ( " " ) ;
						display ( b[2] ) ;
						display ( " " ) ;
						pass ( rm ) ;
					}
					number /= 100 ;
					break ;

				case 5 :
					rm = number % 100 ;
					if ( rm != 0 )
					{
						display ( " " ) ;
						display ( b[3] ) ;
						display ( " " ) ;
						pass ( rm ) ;
					}
					number /= 100 ;
					break ;

			}
			c++ ;
		}

		return string;
	}

	public void pass(int number)
	{
		int rm, q ;
		if ( number < 10 )
		{
			display ( a[number] ) ;
		}

		if ( number > 9 && number < 20 )
		{
			display ( c[number-10] ) ;
		}

		if ( number > 19 )
		{
			rm = number % 10 ;
			if ( rm == 0 )
			{
				q = number / 10 ;
				display ( d[q-2] ) ;
			}
			else
			{
				q = number / 10 ;
				display ( a[rm] ) ;
				display ( " " ) ;
				display ( d[q-2] ) ;
			}
		}
	}

	public void display(String s)
	{
		String t ;
		t= string ;
		string= s ;
		string+= t ;
	}


	public static void main(String args[]){

		Num2WordConvertor num=new Num2WordConvertor();
		System.out.println("num.convertNumToWord(0)"+num.convertNumToWord(0));
		System.out.println("num.convertNumToWord(11)"+num.convertNumToWord(11));
		System.out.println("num.convertNumToWord(22)"+num.convertNumToWord(22));
		System.out.println("num.convertNumToWord(33)"+num.convertNumToWord(33));
		System.out.println("num.convertNumToWord(456899999)"+num.convertNumToWord(456899999));
		System.out.println("num.convertNumToWord(589)"+num.convertNumToWord(589));
		System.out.println("num.convertNumToWord(60000)"+num.convertNumToWord(60000));
		System.out.println("num.convertNumToWord(700000)"+num.convertNumToWord(700000));
		System.out.println("num.convertNumToWord(165535)"+num.convertNumToWord(165535));
		System.out.println("num.convertNumToWord(999565789)"+num.convertNumToWord(999565789));
	}
}
