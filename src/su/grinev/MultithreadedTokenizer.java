package su.grinev;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MultithreadedTokenizer extends Tokenizer {

    @Override
    public List<Token> Tokenize(List<Character> characterList) {
        TaskSplitter taskSplitter = new TaskSplitter();
        List<List<Character>> tasks = taskSplitter.split(characterList, 8);
        List<Future> taskResult = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        Map<Integer, List<Token>> tokensList = new ConcurrentHashMap<>();
        for (int i = 0; i != tasks.size(); i++) {
            int finalI = i;
            taskResult.add(executorService.submit(() -> tokensList.put(finalI, super.Tokenize(tasks.get(finalI)))));
        }
        List<Token> tokenList = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        taskResult.forEach(t -> {
            try {
                t.get();
                tokenList.addAll(tokensList.get(i.getAndIncrement()));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        return tokenList;
    }

}
