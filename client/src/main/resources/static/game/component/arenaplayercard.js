import Component from "/game/component/component.js";
import AvatarManager from "/game/misc/avatarmanager.js";
import SplitLayout from "/game/layout/splitlayout.js";
import Label, {LabelAlignment}  from "/game/component/label.js";
import Image from "/game/component/image.js";
import {roundRect} from "/game/misc/roundrect.js";
import GridLayout from "/game/layout/gridlayout.js";
import ResourceManager from "/game/misc/resourcemanager.js";
import {ProfileAssetPack} from "/game/misc/assets.js";

export default class ArenaPlayerCard extends Component {
    constructor(localeManager) {
        super();

        this.localeManager = localeManager;

        this.backgroundColor = 'rgba(255, 255, 255, 0)';
        this.strokeStyle = 'rgba(255, 255, 255, 1)';

        this.__buildLayout__();
    }

    __buildLayout__() {
        this.wrapperLayout = new GridLayout({rows:1, cols:3});

        this.playerImage = new Image(AvatarManager.getRandomAvatar(Math.floor(Math.random() * 2)));
        this.wrapperLayout.setComponent({row:0, col:0}, this.playerImage);

        this.middleSideLayout = new SplitLayout(false);
        this.wrapperLayout.setComponent({row:0, col:1}, this.middleSideLayout);

        this.nameLabel = new Label(this.localeManager.locale.DEFAULT_USERNAME, LabelAlignment.CENTER);
        this.middleSideLayout.setComponent(0, this.nameLabel);

        this.scoreLabel = new Label("0", LabelAlignment.CENTER);
        this.middleSideLayout.setComponent(1, this.scoreLabel);

        this.statusLabel = new Label(this.localeManager.locale.USER_DISCONNECTED, LabelAlignment.CENTER);

        this.handImage = new Image(ResourceManager.getAsset("5-hand"));
        this.wrapperLayout.setComponent({row:0, col:2}, this.statusLabel);
    }

    setBoundingBox(position, size) {
        super.setBoundingBox(position, size);

        if (this.hasPreferredSize) {
            let optimalSize = {
                width: Math.min(this.size.width, this.preferredSize.width),
                height: Math.min(this.size.height, this.preferredSize.height)
            };


            this.wrapperLayout.setBoundingBox(this.__preferredPosition__, optimalSize);
            this.wrapperLayout.resize(optimalSize);
        } else {
            this.wrapperLayout.setBoundingBox(this.position, this.size);
            this.wrapperLayout.resize(this.size);
        }
    }

    setPreferredSize(preferredSize) {
        super.setPreferredSize(preferredSize);

        this.wrapperLayout.setBoundingBox(this.__preferredPosition__, preferredSize);
        this.wrapperLayout.resize(preferredSize);
    }

    update(deltaTime, inputHandler) {
        this.wrapperLayout.update(deltaTime, inputHandler);
    }

    draw(context) {
        context.fillStyle = this.backgroundColor;
        context.strokeStyle = this.strokeStyle;

        if (this.hasPreferredSize) {
            let optimalSize = {
                width: Math.min(this.size.width, this.preferredSize.width),
                height: Math.min(this.size.height, this.preferredSize.height)
            };

            roundRect(context, this.__preferredPosition__.x, this.__preferredPosition__.y,
                optimalSize.width, optimalSize.height, {
                    tl: 50,
                    br: 25
                }, true);
        } else {
            roundRect(context, this.position.x, this.position.y,
                this.size.width, this.size.height, {
                    tl: 50,
                    br: 25
                }, true);
        }

        this.wrapperLayout.draw(context);
    }

    __changeLastComponent__(isStatus) {
         if (isStatus && !this.showingStatus) {
            this.wrapperLayout.setComponent({row:0, col:2}, this.statusLabel);
            this.statusLabel.setBoundingBox(this.handImage.position, this.handImage.size);
            this.showingStatus = true;
        } else if (!isStatus && this.showingStatus) {
            this.wrapperLayout.setComponent({row:0, col: 2}, this.handImage);
            this.handImage.setBoundingBox(this.statusLabel.position, this.statusLabel.size);
            this.showingStatus = false;
        }
    }

    setPlayer(user) {
        this.nameLabel.text = user.username;

        this.playerImage.setImage(ResourceManager.getAsset(ProfileAssetPack[user.profileAssetIndex]));

        this.__changeLastComponent__(false);

        this.setPlayerHand(5);

        this.backgroundColor = "rgba(50, 131, 168, 0.80)";
    }

    unsetPlayer() {
        this.nameLabel.text = this.localeManager.locale.DEFAULT_USERNAME;

        this.playerImage.setImage(ResourceManager.getAsset("user-unknown"));

        this.__changeLastComponent__(true);

        this.backgroundColor = "rgba(168, 80, 50, 0.80)";
    }

    setPlayerHand(cardNumber) {
        switch (cardNumber) {
            case 0: this.handImage.setImage(null); break;
            case 1: this.handImage.setImage(ResourceManager.getAsset("1-hand")); break;
            case 2: this.handImage.setImage(ResourceManager.getAsset("2-hand")); break;
            case 3: this.handImage.setImage(ResourceManager.getAsset("3-hand")); break;
            case 4: this.handImage.setImage(ResourceManager.getAsset("4-hand")); break;
            case 5: this.handImage.setImage(ResourceManager.getAsset("5-hand")); break;
            default: break;
        }
    }
}