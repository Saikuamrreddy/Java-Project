import java.util.*;

public class InterlockingImpl implements Interlocking {

    private Map<Integer, String> trackSections;
    private Map<String, Train> trains;
    private Map<Integer, List<Integer>> trackConnections;

    public InterlockingImpl() {
        trackSections = new HashMap<>();
        trains = new HashMap<>();
        trackConnections = new HashMap<>();

        trackConnections.put(1, Arrays.asList(2));
        trackConnections.put(2, Arrays.asList(1, 3, 4));
        trackConnections.put(3, Arrays.asList(2, 4, 5));
        trackConnections.put(4, Arrays.asList(2, 3, 5, 6));
        trackConnections.put(5, Arrays.asList(3, 4, 6, 7));
        trackConnections.put(6, Arrays.asList(4, 5, 7, 8));
        trackConnections.put(7, Arrays.asList(5, 6, 8, 9));
        trackConnections.put(8, Arrays.asList(6, 7, 9, 10));
        trackConnections.put(9, Arrays.asList(7, 8, 10, 11));
        trackConnections.put(10, Arrays.asList(8, 9, 11));
        trackConnections.put(11, Arrays.asList(9, 10));
    }

    @Override
    public void addTrain(String trainName, int entryTrackSection, int destinationTrackSection)
            throws IllegalArgumentException, IllegalStateException {
        if (trains.containsKey(trainName)) {
            throw new IllegalArgumentException("Train name already in use.");
        }
        if (trackSections.containsKey(entryTrackSection)) {
            throw new IllegalStateException("Entry track is already occupied.");
        }
        if (!isValidPath(entryTrackSection, destinationTrackSection)) {
            throw new IllegalArgumentException("No valid path from entry to destination.");
        }

        Train train = new Train(trainName, entryTrackSection, destinationTrackSection, "Passenger"); // Default to Passenger
        trains.put(trainName, train);
        trackSections.put(entryTrackSection, trainName);
    }

    @Override
    public int moveTrains(String[] trainNames) throws IllegalArgumentException {
        int movedTrains = 0;
        for (String trainName : trainNames) {
            Train train = trains.get(trainName);
            if (train == null) {
                throw new IllegalArgumentException("Train name does not exist or is no longer in the rail corridor.");
            }

            int currentSection = train.getCurrentSection();
            int destinationSection = train.getDestinationSection();

            if (currentSection == destinationSection) {
                trackSections.remove(currentSection);
                trains.remove(trainName);
                movedTrains++;
                continue;
            }

            List<Integer> possibleMoves = trackConnections.get(currentSection);
            boolean moved = false;

            for (int nextSection : possibleMoves) {
                if (!trackSections.containsKey(nextSection)) {
                    // Move train to the next section
                    trackSections.remove(currentSection);
                    trackSections.put(nextSection, trainName);
                    train.setCurrentSection(nextSection);
                    movedTrains++;
                    moved = true;
                    break;
                } else {
                    Train nextTrain = trains.get(trackSections.get(nextSection));
                    if ("Passenger".equals(train.getType()) && "Freight".equals(nextTrain.getType())) {
                        List<Integer> nextTrainPossibleMoves = trackConnections.get(nextSection);
                        for (int backSection : nextTrainPossibleMoves) {
                            if (!trackSections.containsKey(backSection) && backSection != currentSection) {
                                trackSections.remove(nextSection);
                                trackSections.put(backSection, nextTrain.getName());
                                nextTrain.setCurrentSection(backSection);
                                trackSections.remove(currentSection);
                                trackSections.put(nextSection, trainName);
                                train.setCurrentSection(nextSection);
                                movedTrains++;
                                moved = true;
                                break;
                            }
                        }
                    }
                }

                if (moved) break;
            }
        }
        return movedTrains;
    }

    @Override
    public String getSection(int trackSection) throws IllegalArgumentException {
        if (!trackSections.containsKey(trackSection)) {
            throw new IllegalArgumentException("Track section does not exist.");
        }
        return trackSections.get(trackSection);
    }

    @Override
    public int getTrain(String trainName) throws IllegalArgumentException {
        if (!trains.containsKey(trainName)) {
            throw new IllegalArgumentException("Train name does not exist.");
        }
        return trains.get(trainName).getCurrentSection();
    }

    private boolean isValidPath(int entry, int destination) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.add(entry);
        visited.add(entry);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (current == destination) {
                return true;
            }
            for (int next : trackConnections.get(current)) {
                if (!visited.contains(next)) {
                    queue.add(next);
                    visited.add(next);
                }
            }
        }
        return false;
    }

    private static class Train {
        private String name;
        private int currentSection;
        private int destinationSection;
        private String type;

        public Train(String name, int currentSection, int destinationSection, String type) {
            this.name = name;
            this.currentSection = currentSection;
            this.destinationSection = destinationSection;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public int getCurrentSection() {
            return currentSection;
        }

        public void setCurrentSection(int currentSection) {
            this.currentSection = currentSection;
        }

        public int getDestinationSection() {
            return destinationSection;
        }

        public String getType() {
            return type;
        }
    }
}