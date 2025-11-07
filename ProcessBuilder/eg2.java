import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
class eg2psp
{
public static void main(String args[])
{
String gccHome=System.getenv("GCC_HOME");
if(gccHome==null || gccHome.length()==0)
{
System.out.println("GCC_HOME enviornment variable not set");
return;
}
Executer2 pb=new Executer2(gccHome);
}
}
class Executer2 extends JFrame
{
private JLabel programLabel;
private JTextArea program;
private JPanel programPanel;

private JLabel errorLabel;
private JTextArea error;
private JPanel errorPanel;

private JLabel inputLabel;
private JTextArea input;
private JPanel inputPanel;

private JLabel outputLabel;
private JTextArea output;
private JPanel outputPanel;

private Container container;
private JPanel centerPanel;
private JPanel bottomPanel;
private JButton compileButton;
private JButton runButton;

private String gcc,gccHome;

Executer2(String gccHome)
{
this.gccHome=gccHome;
if(gccHome.endsWith("\\") || gccHome.endsWith("/"))this.gcc=gccHome+"bin"+File.separator+"gcc.exe";
else this.gcc=gccHome+File.separator+"bin"+File.separator+"gcc.exe";

programLabel=new JLabel("Program");
program=new JTextArea();
programPanel=new JPanel();
programPanel.setLayout(new BorderLayout());
programPanel.add(programLabel,BorderLayout.NORTH);
programPanel.add(program,BorderLayout.CENTER);
programPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

errorLabel=new JLabel("Errors");
error=new JTextArea();
error.setEditable(false);
errorPanel=new JPanel();
errorPanel.setLayout(new BorderLayout());
errorPanel.add(errorLabel,BorderLayout.NORTH);
errorPanel.add(error,BorderLayout.CENTER);
errorPanel.setBorder(BorderFactory.createLineBorder(Color.RED));

inputLabel=new JLabel("Input");
input=new JTextArea();
inputPanel=new JPanel();
inputPanel.setLayout(new BorderLayout());
inputPanel.add(inputLabel,BorderLayout.NORTH);
inputPanel.add(input,BorderLayout.CENTER);
inputPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));

outputLabel=new JLabel("Output");
output=new JTextArea();
output.setEditable(false);
outputPanel=new JPanel();
outputPanel.setLayout(new BorderLayout());
outputPanel.add(outputLabel,BorderLayout.NORTH);
outputPanel.add(output,BorderLayout.CENTER);
outputPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));

compileButton=new JButton("Compile");
runButton=new JButton("Run");

centerPanel=new JPanel();
centerPanel.setLayout(new GridLayout(2,2));
centerPanel.add(programPanel);
centerPanel.add(errorPanel);
centerPanel.add(inputPanel);
centerPanel.add(outputPanel);

JPanel bottomPanel=new JPanel();
bottomPanel.setLayout(new FlowLayout());
bottomPanel.add(compileButton);
bottomPanel.add(runButton);

container=getContentPane();
container.setLayout(new BorderLayout());
container.add(centerPanel,BorderLayout.CENTER);
container.add(bottomPanel,BorderLayout.SOUTH);

setDefaultCloseOperation(EXIT_ON_CLOSE);
setLocation(10,10);
setSize(1024,768);
setVisible(true);

// Button Actions
//compileButton.addActionListener(e -> compileProgram());
//runButton.addActionListener(e -> runProgram());
compileButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
try
{
error.setText("");
output.setText("");

File tmpFolder=new File("tmp");
if(!tmpFolder.exists()) tmpFolder.mkdir();

File sourceFile=new File(tmpFolder,"code.c");
if(sourceFile.exists()) sourceFile.delete();

String code=program.getText().trim();

try(FileWriter fw=new FileWriter(sourceFile))
{
fw.write(code);
}

ProcessBuilder pb=new ProcessBuilder(gcc,"code.c","-o","code.exe");
pb.directory(tmpFolder);
//pb.redirectErrorStream(true);
Process p=pb.start();

BufferedReader br=new BufferedReader(new InputStreamReader(p.getErrorStream()));
StringBuilder sb=new StringBuilder();
String line;
while((line=br.readLine())!=null) sb.append(line+"\n");

int exitCode=p.waitFor();
if(exitCode==0)
{
output.setForeground(Color.GREEN);
output.setText("Compilation Successfull");
}
else
{
output.setForeground(Color.RED);
output.setText("Compilation Failed\n"+sb.toString());
}

}catch(Exception e)
{
output.setText("Error : "+e.getMessage());
}
}
});

runButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
try
{
error.setText("");
output.setText("");

File tmpFolder=new File("tmp");
if(!tmpFolder.exists()) 
{
error.setText("No Source Code Exists");
return;
}
File exeFile=new File(tmpFolder,"code.exe");
if(!exeFile.exists())
{
error.setText("Source Code is Not Compiled..\nCompile First...");
return;
}

ProcessBuilder pb=new ProcessBuilder("cmd.exe","/c",exeFile.getAbsolutePath());
pb.directory(tmpFolder);
Process process=pb.start();

        // Send input to program
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
            String in = input.getText().trim();
            if (!in.isEmpty()) {
                bw.write(in);
                bw.newLine();
            }
            bw.flush();
        }
        // Read both stdout and stderr
        BufferedReader brOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader brErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = brOut.readLine()) != null) sb.append(line).append("\n");
        while ((line = brErr.readLine()) != null) sb.append(line).append("\n");
int exitCode=process.waitFor();
        sb.append("\nProcess exited with code: ").append(exitCode);

        output.setText(sb.toString());


}catch(Exception e)
{
output.setText("Error : "+e.getMessage());
}

}
});

}
}