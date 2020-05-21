import {BackTextures, CardTextures, HandTextures} from '/game/misc/assets.js';
import {MaleAvatars, FemaleAvatars, UnknownAvatars} from '/game/misc/assets.js';

export default class ResourceManager {
    static loadAssets() {
        let gameAssets = $("#game-assets")[0];

        // deck
        gameAssets.innerHTML += this.__loadImageAssets__("/assets/images/cards/backs/", BackTextures);
        gameAssets.innerHTML += this.__loadImageAssets__("/assets/images/cards/hand/", HandTextures);
        gameAssets.innerHTML += this.__loadImageAssets__("/assets/images/cards/pack/", CardTextures);

        // avatars
        gameAssets.innerHTML += this.__loadImageAssets__("/assets/images/users/", MaleAvatars);
        gameAssets.innerHTML += this.__loadImageAssets__("/assets/images/users/", FemaleAvatars);
        gameAssets.innerHTML += this.__loadImageAssets__("/assets/images/users/", UnknownAvatars);
    }

    static getAsset(assetId) {
        return document.getElementById(assetId);
    }

    static __loadImageAssets__(dir, assets) {
        let result;

        for (let i = 0; i < assets.size; ++i) {
            result += `<img src="${dir}${assets.files[i]}" th:src="@{${dir}${assets.files[i]}}" id="${assets.ids[i]}" />`;
        }

        return result;
    }
}
