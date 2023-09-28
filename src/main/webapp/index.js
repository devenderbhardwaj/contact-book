import { HeaderView } from "./js/views/HeaderView.js";
import { MainView } from "./js/views/MainView.js";

const root = document.getElementById("root");

const header = new HeaderView();
const main = new MainView();
root.append(header.getViewElement());
header.bindOnSearch(main.search);
root.append(main.getViewElement());