package com.example.demo.util;

import org.springframework.stereotype.Component;

import com.example.demo.enums.Action;
import com.example.demo.enums.Goto;
import com.example.demo.enums.State;

@Component
public class EnumUtil { //Enum的轉換工具

    public static <E extends Enum<E>> String getDisplayName(E e) {
        if (e instanceof State) {
            return ((State) e).getDisplayName();
        }
        if (e instanceof Action) {
            return ((Action) e).getDisplayName();
        }
        if (e instanceof Goto) {
            return ((Goto) e).getDisplayName();
        }
        // 可擴充更多 enum
        return e.name(); // fallback
    }

    public static <E extends Enum<E>> E fromString(Class<E> enumClass, String name) {
        return Enum.valueOf(enumClass, name);
    }

    public static <E extends Enum<E>> E fromCode(Class<E> enumClass, int code) {
        if (enumClass == State.class) {
            return (E) State.fromCode(code);
        }
        if (enumClass == Action.class) {
            return (E) Action.fromCode(code);
        }
        throw new IllegalArgumentException("不支援的 enum 類型");
    }
}

