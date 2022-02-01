package su.grinev;

import java.util.ArrayList;
import java.util.List;

public class TaskSplitter {

    public List<List<Character>> split(List<Character> characterList, int parts) {
        List<List<Character>> tasks = new ArrayList<>();
        int size = characterList.size() / parts;
        for (int i = 0; i != parts; i++) {
            tasks.add(new ArrayList<>());
            splitWithPadding(characterList, tasks.get(i), size);
        }
        return tasks;
    }

    private void splitWithPadding(List<Character> from, List<Character> to, int min) {
        if (from.size() == 0) {
            return;
        }
        min = Math.min(min, from.size());
        to.addAll(from.subList(0, min));
        from.subList(0, min).removeAll(from);
        if (from.size() == 0) return;
        char lastChar = from.get(0);
        while (lastChar != ':' && lastChar != '{' && lastChar != '}' && lastChar != '[' && lastChar != ']') {
            lastChar = from.remove(0);
            to.add(lastChar);
        }
    }

}
