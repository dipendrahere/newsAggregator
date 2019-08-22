package code.clusteringComponent;

import code.models.Article;
import code.models.Cluster;
import org.apache.commons.lang.NullArgumentException;

import java.util.Collection;
import java.util.List;

public interface Clusterer <T extends Article> {
    public List<Cluster<T>> cluster(final List<T> points) throws NullArgumentException;
}