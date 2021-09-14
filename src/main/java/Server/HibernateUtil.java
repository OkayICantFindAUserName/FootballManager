package Server;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//Hilfsklasse für hibernate
public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Erstellen einer Registry
                registry = new StandardServiceRegistryBuilder().configure().build();
                // Erstellen der MetadataSource
                MetadataSources sources = new MetadataSources(registry);

                // Erstellen der Metadata
                Metadata metadata = sources.getMetadataBuilder().build();
                // Erstellen der SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    // Fehler wurde gecached nachdem Registry erstellt wurde destroy Sie.
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
