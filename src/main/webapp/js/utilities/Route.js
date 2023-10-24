export default class Route {
    path;
    navigateTo;

    /**
     * 
     * @param {string} pth 
     * @param {navig} ngt 
     */
    constructor(pth, ngt) {
        this.path = pth;
        this.navigateTo = ngt;
    }

    /**
     * @callback navig
     * @param {URL} url
     */
}