import ResourceManager from "/game/misc/resourcemanager.js";
import {MaleAvatars, FemaleAvatars, UnknownAvatars} from "/game/misc/assets.js";

const Gender = {
    MALE: 0,
    FEMALE: 1
};

export default class AvatarManager {
    static getRandomAvatar(gender) {
        if (gender === Gender.MALE) {
            let randomId = Math.floor(Math.random() * MaleAvatars.size);
            return ResourceManager.getAsset(MaleAvatars.ids[randomId]);
        } else {
            let randomId = Math.floor(Math.random() * FemaleAvatars.size);
            return ResourceManager.getAsset(MaleAvatars.ids[randomId]);
        }
    }

    static getUnknownAvatar() {
        return ResourceManager.getAsset(UnknownAvatars.ids[0]);
    }
}