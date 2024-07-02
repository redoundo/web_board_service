import axios from "axios";

export default {
	async validation({ path, token, onSuccess, onError }) {
		return await axios
			.get(path, { headers: { Authorization: token } })
			.catch((err) => onError(err))
			.then((value) => onSuccess(value));
	},
	async signIn({ path, id, password, nickname, onSuccess, onError }) {
		return await axios
			.post(path, { id: id, password: password, nickname: nickname })
			.then((value) => onSuccess(value))
			.catch((err) => onError(err));
	},
	async login({ path, id, password, onSuccess, onError }) {
		return await axios
			.post(path, { id: id, password: password })
			.then((value) => onSuccess(value))
			.catch((err) => onError(err));
	},
	async canUseNickname({ path, nickname, onSuccess, onError }) {
		return await axios
			.post(path, { nickname: nickname })
			.then((val) => onSuccess(val))
			.catch((err) => onError(err));
	},
};
