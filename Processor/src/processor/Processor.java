package processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

class RegisterStatus{
    public int qi;
    public boolean status;
}

class ReservationStation{
    public String name;
    public boolean busy;
    public Operation op;
    public int vj;
    public int vk;
    public int qj;
    public int qk;
    public int dest;
    public String A;
}

class ReorderBuffer{
    public boolean busy;
    public Operation instruction;
    public State state;
    public int destination;
    public String value;
}

public class Processor {
    
    private int N_RegisterStatus = 32;
    private int N_ReservationStation = 12;
    
    private RegisterStatus registerStatus[] = new RegisterStatus[N_RegisterStatus];
    private Vector<Command> commands;
    private Vector<ReorderBuffer> ro;
    private ReservationStation reservationStations[] = new ReservationStation[N_ReservationStation];
    
    private void readFile(){
        
        Command.setMap();
        String filename = "program.txt";
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                commands.add(Command.parseCommand(line.split(" ")[0]));
            }
        }catch(IOException e){
            System.out.println("Erro na leitura");
        }
    }    
    
    public Processor(){
        readFile();
    }
    
    public void issue (Command New_Instruction, int Instruction_index)
    {
        int i;
        for (i = 0; i < N_ReservationStation; i++)
            if (!reservationStations[i].busy)
                break;
        
        if (i < N_ReservationStation)    //Existe uma estação de reserva vazia.
        {
            reservationStations[i].busy = true;
            New_Instruction.T_Dest = i;
            if (New_Instruction.isR())
            {
                reservationStations[i].qj = FindCorrespondingSource(New_Instruction.rs, Instruction_index);
                reservationStations[i].qk = FindCorrespondingSource(New_Instruction.rt, Instruction_index);
            }
            
            if (New_Instruction.isI())
            {
                reservationStations[i].qj = FindCorrespondingSource(New_Instruction.rd, Instruction_index);
            }
            
        }
       
    }
    
    //Percorre todos os índices de 0 a Instruction_index - 1, procurando a instrução com maior índice
    //de modo que Source_Register seja o registrador de destino, e retorna o índice da estação de reserva
    //correspondente.
    public int FindCorrespondingSource (int Source_Register, int Instruction_index)
    {
        for (int i = Instruction_index - 1; i >= 0; i++)
            if (commands.get(i).rd == Source_Register)
                return commands.get(i).T_Dest;
        return Source_Register;    
    }
    
    public void nextClock(){
        
    }
    
    
}
