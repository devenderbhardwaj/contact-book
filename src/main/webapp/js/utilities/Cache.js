export class LRUCache {
    #cache;
    #capacity;

    constructor(capacity) {
        this.#cache = new Map();
        this.#capacity = capacity;
    }

    /**
     * Return the value associated with key
     * @param {any} key 
     * @returns {any}
     */
    get(key) {
        if (this.#cache.has(key)) {
            const value = this.#cache.get(key);

            // Refresh the key to mark it as recently used
            this.#cache.delete(key);
            this.#cache.set(key, value);
            return value;
        }
        return null;
    }

    /**
     * Return true if key exist in Cache, false otherwise
     * @param {any} key 
     * @returns {boolean}
     */
    has(key) {
        return this.#cache.has(key);
    }
    
    /**
     * Associate the value with key, if key already exist then older value is deleted
     * @param {any} key 
     * @param {any} value 
     */
    set(key, value) {
        if (this.#cache.has(key)) {

            // If the key already exists, remove it to update the value
            this.#cache.delete(key);
        } else if (this.#cache.size >= this.#capacity) {
            // If the cache is full, remove the least recently used item
            const oldestKey = this.#cache.keys().next().value;
            this.#cache.delete(oldestKey);
        }
        this.#cache.set(key, value);
    }
}
