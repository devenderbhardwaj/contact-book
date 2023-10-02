import { MainContentArea } from "./js/views/MainContentArea.js";
import { SideBarView } from "./js/views/SideBarView.js";

const root = document.getElementById("root");


const sidebar = new SideBarView();
const mainContentArea = new MainContentArea();

root.appendChild(sidebar.getViewElement());
root.appendChild(mainContentArea.getViewElement());


sidebar.bindOnContactCreate(() => mainContentArea.createContact());
