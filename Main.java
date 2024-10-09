
public class Main {

    public static void main(String[] args) {
        Interlocking interlocking = new InterlockingImpl();

        // Adding trains
        try {
            interlocking.addTrain("PassengerTrainA", 1, 4);
            interlocking.addTrain("PassengerTrainB", 3, 11);
            interlocking.addTrain("FreightTrainC", 4, 2);
            interlocking.addTrain("FreightTrainD", 9, 2);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error adding train: " + e.getMessage());
        }

        // Moving trains
        try {
            int movedTrains = interlocking.moveTrains(new String[]{"PassengerTrainA", "PassengerTrainB", "FreightTrainC", "FreightTrainD"});
            System.out.println("Number of trains moved: " + movedTrains);
        } catch (IllegalArgumentException e) {
            System.out.println("Error moving trains: " + e.getMessage());
        }

        // Checking track sections
        try {
            System.out.println("Section 2 is occupied by: " + interlocking.getSection(2));
            System.out.println("Section 4 is occupied by: " + interlocking.getSection(4));
            System.out.println("Section 9 is occupied by: " + interlocking.getSection(9));
        } catch (IllegalArgumentException e) {
            System.out.println("Error getting section: " + e.getMessage());
        }

        // Checking train positions
        try {
            System.out.println("PassengerTrainA is at section: " + interlocking.getTrain("PassengerTrainA"));
            System.out.println("PassengerTrainB is at section: " + interlocking.getTrain("PassengerTrainB"));
            System.out.println("FreightTrainC is at section: " + interlocking.getTrain("FreightTrainC"));
            System.out.println("FreightTrainD is at section: " + interlocking.getTrain("FreightTrainD"));
        } catch (IllegalArgumentException e) {
            System.out.println("Error getting train: " + e.getMessage());
        }
    }
}