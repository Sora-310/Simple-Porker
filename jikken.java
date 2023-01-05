import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;

class jikken extends JFrame
{
	public static void main(String[] args)
	{
		final int[] n = new int[1];
		n[0] = 3;

		System.out.println(n[0]);

		aaa(n);

		System.out.println(n[0]);
	}

	public static void aaa(int[] num)
	{
		num[0] = 9999;
	}
}
