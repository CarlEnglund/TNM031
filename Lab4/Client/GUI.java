import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.io.*;
import java.util.*;


public class GUI extends JFrame implements ActionListener
{
	// instansvariabler för gissning
	public JLabel nameLabel, socialSecurityLabel, votingOptionLabel, voteCompleted;
	public JButton castVote, verifyVote, seeResult;
	public JTextField name, socialSecurityNumber;
	public Client test;
	public JRadioButton button1, button2, button3, button4;
	public ButtonGroup bg;


	public GUI()
	{
		// sätt text på allt som ska ut
		nameLabel = new JLabel("Name");
		socialSecurityLabel = new JLabel("Social Security Number");
		votingOptionLabel = new JLabel("Option");
		voteCompleted = new JLabel("");
		

		//Textfields
		name = new JTextField();
		socialSecurityNumber = new JTextField();

		castVote = new JButton("Vote");
		verifyVote = new JButton("Verify Vote");
		seeResult = new JButton("See Result");

		seeResult.setEnabled(false);

		button1 = new JRadioButton("Alternativ 1");
		button2 = new JRadioButton("Alternativ 2");
		button3 = new JRadioButton("Alternativ 3");
		button4 = new JRadioButton("Alternativ 4");

		bg = new ButtonGroup();
		bg.add(button1);
		bg.add(button2);
		bg.add(button3);
		bg.add(button4);

		castVote.addActionListener(this);
		verifyVote.addActionListener(this);
		seeResult.addActionListener(this);
		
		// initiera getContentPane
		Container c = getContentPane();
		// sätt layout, tre rader två kolumner
		c.setLayout(new GridBagLayout());
		GridBagConstraints d = new GridBagConstraints();
		d.fill = GridBagConstraints.HORIZONTAL;

		d.fill = GridBagConstraints.HORIZONTAL;
		d.gridx = 0;
		d.gridy = 0;
		d.insets = new Insets(25,10,0,10);
		c.add(nameLabel, d); 

		d.fill = GridBagConstraints.HORIZONTAL;
		d.weightx = 0.5;		
		d.gridx = 1;
		d.gridy = 0;
		d.insets = new Insets(25,10,0,10);
		c.add(name, d); 

		d.fill = GridBagConstraints.HORIZONTAL;
		d.gridx = 0;
		d.gridy = 1;
		d.insets = new Insets(0,10,0,10);
		c.add(socialSecurityLabel, d); 

		d.fill = GridBagConstraints.HORIZONTAL;
		d.weightx = 0.5;
		d.insets = new Insets(0,10,0,10);
		d.gridx = 1;
		d.gridy = 1;
		c.add(socialSecurityNumber, d);

		d.fill = GridBagConstraints.HORIZONTAL;
		d.gridx = 0;
		d.gridy = 2;
		c.add(button1, d);

		d.fill = GridBagConstraints.HORIZONTAL;
		d.weightx = 0.5;
		d.gridx = 1;
		d.gridy = 2;
		c.add(button2, d);

		d.fill = GridBagConstraints.HORIZONTAL;
		d.gridx = 0;
		d.gridy = 3;
		c.add(button3, d);

		d.fill = GridBagConstraints.HORIZONTAL;
		d.weightx = 0.5;
		d.gridx = 1;
		d.gridy = 3;
		c.add(button4, d); 

		d.fill = GridBagConstraints.HORIZONTAL;
		d.weightx = 0.5;
		d.ipady = 100;    
		d.gridx = 0;
		d.gridy = 4;
		d.gridwidth = 2;
		c.add(voteCompleted, d); 

		d.fill = GridBagConstraints.HORIZONTAL;
		d.gridx = 0;
		d.gridy = 5;
		d.ipady = 100;      
		d.weightx = 0.0;
		d.weighty = 0.7;
		d.gridwidth = 2;
		//d.anchor = GridBagConstraints.PAGE_END; //bottom of space
		d.insets = new Insets(10,10,10,10);

		c.add(castVote, d);

		d.fill = GridBagConstraints.HORIZONTAL;
		d.gridx = 0;
		d.gridy = 6;
		d.ipady = 100;      
		d.weightx = 0.0;
		d.weighty = 0.7;
		d.gridwidth = 2;
		//d.anchor = GridBagConstraints.PAGE_END; //bottom of space
		d.insets = new Insets(10,10,10,10);
		c.add(verifyVote, d);

		d.fill = GridBagConstraints.HORIZONTAL;
		d.gridx = 0;
		d.gridy = 7;
		d.ipady = 100;      
		d.weightx = 0.0;
		d.weighty = 0.7;
		d.gridwidth = 2;
		//d.anchor = GridBagConstraints.PAGE_END; //bottom of space
		d.insets = new Insets(10,10,10,10);
		c.add(seeResult, d);

		

		setTitle("Secure Virtual Election");
		setSize(450, 450);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		test = new Client(); 
		test.run();


	}

	public void actionPerformed(ActionEvent e)
	{
		
		if(e.getSource() == castVote) {	

			String vote = getSelectedButton();
			test.MessageToCLA(name.getText(), socialSecurityNumber.getText(), "go");	
			test.MessageToCTF(test.getMessageFromCLA(), socialSecurityNumber.getText(), vote);
		
				String check = test.getMessageFromCTF();
				voteCompleted.setText(check);

				if(check.equals("Voting Completed")) {
					castVote.setEnabled(false);
					seeResult.setEnabled(true);
				}
		}

		if(e.getSource() == verifyVote) {
			test.MessageToCLA(name.getText(), socialSecurityNumber.getText(), "verify");	
			test.MessageToCTF("Verify", test.getMessageFromCLA(), socialSecurityNumber.getText());
			voteCompleted.setText(test.getMessageFromCTF());
		}

		if(e.getSource() == seeResult) {
			test.MessageToCTF("Result", "", "");
			String results = "<html><body>";
			results += test.getMessageFromCTF();
			results = results.replaceAll(":", "<br>");
			results += "</body></html>";
			voteCompleted.setText(results);
			seeResult.setEnabled(true);
		}


	}

	private String getSelectedButton() {

		Enumeration<AbstractButton> allRadioButton=bg.getElements();  
        while(allRadioButton.hasMoreElements())  
        {  
           JRadioButton temp=(JRadioButton)allRadioButton.nextElement();  
           if(temp.isSelected()) {  
           		return temp.getText();
           }  
        }            
        return "No alternative";
       
	}
	// huvudprogram
	public static void main(String[] args)
	{
	
		GUI frame = new GUI();
		
		
	}
}
