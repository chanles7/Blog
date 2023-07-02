package com.java.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * description 功能描述
 *
 * @author ch
 * create 2023/7/1 7:28
 */
@Component
public class SensitiveFilterUtil {

    private static final List<String> sensitiveList = new ArrayList<>();

    private static final Map<String, Object> sensitiveTree = new HashMap<>();


    @PostConstruct
    public void init() throws IOException {
        initSensitiveList();

        initSensitiveTree();
    }


    private void initSensitiveList() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
        assert inputStream != null;
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            line = line.replaceAll(" ", "");
            sensitiveList.add(line);
        }
        scanner.close();
    }


    private void initSensitiveTree() {
        for (String word : sensitiveList) {
            Map<String, Object> tree = sensitiveTree;
            char[] chars = word.toCharArray();
            int i = 0;
            for (char ch : chars) {
                i++;
                String keyWord = String.valueOf(ch);
                if (tree.containsKey(keyWord)) {
                    tree = (Map<String, Object>) tree.get(keyWord);
                } else {
                    Map<String, Object> map = new HashMap<>();
                    tree.put(keyWord, map);
                    tree = map;
                }
                if (i == chars.length) {
                    tree.put("is_end", "1");
                }
            }
        }
    }


    public static String filter(String content) {
        StringBuilder val = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        Map<String, Object> tree = sensitiveTree;
        boolean flag = false;
        char[] chars = content.toCharArray();
        for (char ch : chars) {
            String word = String.valueOf(ch);
            if (Arrays.asList("*", " ").contains(word)) continue;
            if (tree.containsKey(word)) {
                temp.append(word);
                if (flag) {
                    if (!"*".equals(String.valueOf(val.charAt(val.length() - 1)))) {
                        val.replace(val.length() - 1, val.length(), "*");
                    }
                    val.append("*");
                } else {
                    flag = true;
                    val.append(word);
                }
                tree = (Map<String, Object>) tree.get(word);
                if ("1".equals(tree.get("is_end"))) {
                    temp = new StringBuilder();
                }
            } else {
                if (temp.length() > 0) {
                    val.replace(val.length() - temp.length(), val.length(), temp.toString());
                    temp = new StringBuilder();
                }
                tree = sensitiveTree;
                if (flag && tree.containsKey(word)) {
                    temp.append(word);
                    tree = (Map<String, Object>) tree.get(word);
                } else {
                    flag = false;
                }
                val.append(word);
            }
        }
        return val.toString();
    }
}
