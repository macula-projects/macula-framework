package org.macula.boot;

import org.springframework.context.ApplicationEvent;

public class ApplicationContext extends ApiApplicationContext {
    private ApplicationContext() {
        // Noops
    }

    /**
     * 获取bean
     *
     * @param name
     * @return Object
     */
    public static <T> T getBean(String name) {
        try {
            return (T) getContainer().getBean(name);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取bean
     *
     * @param name bean name
     * @param clz  bean class
     * @return Object
     */
    public static <T> T getBean(String name, Class<T> clz) {
        return getContainer().getBean(name, clz);
    }

    /**
     * 获取bean
     *
     * @param clz bean class
     * @return Object
     */
    public static <T> T getBean(Class<T> clz) {
        return getContainer().getBean(clz);
    }


    public static void publishApplicationEvent(final ApplicationEvent event) {
        if (event != null && getContainer() != null) {
            getContainer().publishEvent(event);
        }
    }
}
