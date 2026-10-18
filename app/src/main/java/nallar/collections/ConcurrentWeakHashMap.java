package nallar.collections;

import java.util.*;

/**
 * Weak-keyed map that is safe to use from several threads.
 * All access goes through one lock and the key/value/entry views are snapshots,
 * so iterating while other threads (or listeners) add entries never throws
 * ConcurrentModificationException. Replaces the earlier Guava MapMaker based version.
 */
public class ConcurrentWeakHashMap extends WeakHashMap {
	private final Map map = new WeakHashMap();

	public Object putIfAbsent(final Object key, final Object value) {
		synchronized (map) {
			Object existing = map.get(key);
			if (existing == null) {
				map.put(key, value);
			}
			return existing;
		}
	}

	public Object replace(final Object key, final Object value) {
		synchronized (map) {
			return map.containsKey(key) ? map.put(key, value) : null;
		}
	}

	public boolean remove(final Object key, final Object value) {
		synchronized (map) {
			if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
				map.remove(key);
				return true;
			}
			return false;
		}
	}

	public boolean replace(final Object key, final Object oldValue, final Object newValue) {
		synchronized (map) {
			if (map.containsKey(key) && Objects.equals(map.get(key), oldValue)) {
				map.put(key, newValue);
				return true;
			}
			return false;
		}
	}

	@Override
	public int size() {
		synchronized (map) {
			return map.size();
		}
	}

	@Override
	public boolean isEmpty() {
		synchronized (map) {
			return map.isEmpty();
		}
	}

	@Override
	public boolean containsKey(final Object key) {
		synchronized (map) {
			return map.containsKey(key);
		}
	}

	@Override
	public boolean containsValue(final Object value) {
		synchronized (map) {
			return map.containsValue(value);
		}
	}

	@Override
	public Object get(final Object key) {
		synchronized (map) {
			return map.get(key);
		}
	}

	@Override
	public Object put(final Object key, final Object value) {
		synchronized (map) {
			return map.put(key, value);
		}
	}

	@Override
	public Object remove(final Object key) {
		synchronized (map) {
			return map.remove(key);
		}
	}

	@Override
	public void putAll(final Map m) {
		synchronized (map) {
			map.putAll(m);
		}
	}

	@Override
	public void clear() {
		synchronized (map) {
			map.clear();
		}
	}

	@Override
	public Set keySet() {
		synchronized (map) {
			return new HashSet(map.keySet());
		}
	}

	@Override
	public Collection values() {
		synchronized (map) {
			return new ArrayList(map.values());
		}
	}

	@Override
	public Set<Map.Entry> entrySet() {
		synchronized (map) {
			return new HashSet<Map.Entry>(map.entrySet());
		}
	}

	@Override
	public boolean equals(final Object o) {
		synchronized (map) {
			return map.equals(o);
		}
	}

	@Override
	public int hashCode() {
		synchronized (map) {
			return map.hashCode();
		}
	}
}
