/**
 * Provides a user-friendly form to provide inputs for custom scripts called within the editor
 * Put this class file in the folder org in WPCore.jar
 *
 * @author creativitRy
 * Date: 8/12/2016.
 */

package org;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScriptExtension
{
	public static HashMap<String, JComponent> map = new HashMap<>();
	public static String title = "Title";

	/**
	 * Tests the method
	 *
	 * @param args
	 */
	public static void main(String[] args)
	{
		String[] test = {"displayName, TestScript", "testLo ng,test,test", "test2,10", "test3,10.7,test3,9.1//12.2//0.25", "label, test label", "test4, TERRAIN1, tool", "t,ENUM:entry1//entry2//entry3//entry4", "bool, false"};
		System.out.println(Arrays.toString(form(test)));
	}

	/**
	 * Provides a user-friendly form to provide inputs
	 *
	 * @param entries the array of inputs in this specific format "name of field, default value, [tooltip], [[min]//max//[step]]"
	 *                the part in square brackets are optional
	 *                if the name of field is displayName, the default value will become the name of the input window.
	 *                default values can be:
	 *                -an integer
	 *                -a float
	 *                -a string (if name of field is label, the string won't be editable, and it will assume there is no tooltip)
	 *                -an enum (in this format "[0]ENUM:entry1//entry2//entry3//entry4") where the number before ENUM is the selected index (default is 0)
	 *                -a boolean (checkbox for either "true" or "false")
	 *                -TODO:a terrain (in this format "TERRAIN:IndexNumber")
	 *                -TODO:a layer
	 *                tooltip is the info displayed. empty string or null will not be displayed
	 *                min//max//step are for number values. each value can be null (blank) (when step is null or <=0, it will be 1 for int and 0.1 for float). default is null//null//null. can be inputted max, min and max, or min and max and step
	 * @return all values in a String array where each String is formatted as "name of field==value returned"
	 */
	public static String[] form(String[] entries)
	{
		Box vBox = new Box(BoxLayout.Y_AXIS);

		for (String entry2 : entries)
		{
			String[] entry = entry2.split(",");

			if (entry[0].equals("label")) //label
			{
				JLabel label = new JLabel("<html>" + entry[1] + "</html>", JLabel.TRAILING);
				vBox.add(label);
				continue;
			}
			else if (entry[0].equals("displayName")) //title
			{
				title = entry[1].trim();
				continue;
			}

			Box hBox = new Box(BoxLayout.X_AXIS);

			JLabel label = new JLabel(entry[0] + " ", JLabel.RIGHT);
			hBox.add(label);

			if (entry.length < 2)
			{
				String temp = entry[0];
				entry = new String[2];
				entry[0] = temp;
			}


			entry[1] = entry[1].trim();

			if (entry[1].contains("ENUM:"))
			{
				String[] enums = entry[1].substring(entry[1].indexOf("ENUM:") + 5).split("//");
				JComboBox comboBox = new JComboBox(enums);

				try
				{
					comboBox.setSelectedIndex(Integer.parseInt(entry[1].substring(0, entry[1].indexOf("ENUM:"))));
				} catch (Exception e)
				{
					//
				}


				if (entry.length > 2 && entry[2].length() > 0)
					comboBox.setToolTipText(entry[2]);

				hBox.add(comboBox);
				map.put(entry[0], comboBox);
			}
			/*else if (entry[1].contains("TERRAIN:")) //Terrain
			{
				JComboBox comboBox = new JComboBox();
				comboBox.setModel(new DefaultComboBoxModel(Terrain.PICK_LIST));
				//TODO:comboBox.setRenderer(new TerrainListCellRenderer(new DynMapColourScheme() ));

				comboBox.setSelectedIndex(Integer.parseInt(entry[1].substring(entry[1].indexOf("TERRAIN:") + 8)));

				if (entry.length > 2 && entry[2].length() > 0)
					comboBox.setToolTipText(entry[2]);

				hBox.add(comboBox);
			}*/
			else
			{
				try //int
				{
					int value = Integer.parseInt(entry[1].trim());

					SpinnerNumberModel model = new SpinnerNumberModel(value, null, null, 1);
					if (entry.length > 3)
					{
						String[] vals = entry[3].split("//");

						if (vals.length == 1)
						{
							if (vals[0].length() == 0)
								model.setMaximum(null);
							else
								model.setMaximum(Integer.parseInt(vals[0]));
						}
						else
						{
							if (vals[0].length() == 0)
								model.setMinimum(null);
							else
								model.setMinimum(Integer.parseInt(vals[0]));

							if (vals[1].length() == 0)
								model.setMaximum(null);
							else
								model.setMaximum(Integer.parseInt(vals[1]));

							if (vals.length == 3)
							{
								if (vals[2].equals(""))
									model.setStepSize(1);
								else
									model.setStepSize(Integer.parseInt(vals[2]));

								if (model.getStepSize().intValue() < 1)
									model.setStepSize(1);
							}
						}
					}

					JSpinner spinner = new JSpinner(model);

					if (entry.length > 2 && entry[2].length() > 0)
						spinner.setToolTipText(entry[2]);

					hBox.add(spinner);
					map.put(entry[0], spinner);
				} catch (NumberFormatException e)
				{
					try //float
					{
						double value = Double.parseDouble(entry[1].trim());

						SpinnerNumberModel model = new SpinnerNumberModel(value, null, null, 0.1);
						if (entry.length > 3)
						{
							String[] vals = entry[3].split("//");

							if (vals.length == 1)
							{
								if (vals[0].length() == 0)
									model.setMaximum(null);
								else
									model.setMaximum(Double.parseDouble(vals[0]));
							}
							else
							{
								if (vals[0].length() == 0)
									model.setMinimum(null);
								else
									model.setMinimum(Double.parseDouble(vals[0]));

								if (vals[1].length() == 0)
									model.setMaximum(null);
								else
									model.setMaximum(Double.parseDouble(vals[1]));

								if (vals.length == 3)
								{
									if (vals[2].length() == 0)
										model.setStepSize(0.1);
									else
										model.setStepSize(Double.parseDouble(vals[2]));

									if (model.getStepSize().doubleValue() <= 0)
										model.setStepSize(0.1);
								}
							}
						}

						JSpinner spinner = new JSpinner(model);

						if (entry.length > 2 && entry[2].length() > 0)
							spinner.setToolTipText(entry[2]);

						hBox.add(spinner);
						map.put(entry[0], spinner);
					} catch (Exception ee)
					{
						if (entry[1].equals("true") || entry[1].equals("false"))
						{//boolean
							JCheckBox checkBox = new JCheckBox("", entry[1].equals("true"));

							if (entry.length > 2 && entry[2].length() > 0)
								checkBox.setToolTipText(entry[2]);

							hBox.add(checkBox);
							map.put(entry[0], checkBox);
						}
						else
						{//string

							JTextField textField = new JTextField(entry[1].trim());

							if (entry.length > 2 && entry[2].length() > 0)
								textField.setToolTipText(entry[2]);

							hBox.add(textField);
							map.put(entry[0], textField);
						}

					}

				}

			}


			vBox.add(hBox);
		}

		int result = JOptionPane.showConfirmDialog(null, vBox, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (result != JOptionPane.OK_OPTION)
			return null;

		String[] results = new String[map.size()];
		int i = 0;
		for (Map.Entry<String, JComponent> mapEntry : map.entrySet())
		{
			if (mapEntry.getValue() instanceof JComboBox)
				results[i] = mapEntry.getKey() + "==" + ((JComboBox) mapEntry.getValue()).getSelectedItem();
			else if (mapEntry.getValue() instanceof JSpinner)
				results[i] = mapEntry.getKey() + "==" + ((JSpinner) mapEntry.getValue()).getValue();
			else if (mapEntry.getValue() instanceof JCheckBox)
				results[i] = mapEntry.getKey() + "==" + (((JCheckBox) mapEntry.getValue()).isSelected() ? "true" : "false");
			else
				results[i] = mapEntry.getKey() + "==" + ((JTextField) mapEntry.getValue()).getText();

			i++;
		}

		return results;
	}

}