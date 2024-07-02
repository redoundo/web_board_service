import { checkString } from "./checkValid.js";

export const jwtParser = {
	get() {
		console.log(window.$cookies?.get("Authorization"));
		return window.$cookies?.get("Authorization");
	},
	set(token) {
		window.$cookies.set("Authorization", token, 6000000);
		console.log(window.$cookies?.get("Authorization"));
	},
	remove() {
		window.$cookies.remove("Authorization");
		console.log(window.$cookies?.get("Authorization"));
	},
	validity(token) {
		return checkString(token);
	},
};
