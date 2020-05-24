import BorderLayout from "/game/layout/borderlayout.js";
import SplitLayout from "/game/layout/splitlayout.js";
import Label, {LabelAlignment} from "/game/component/label.js";
import MarginLayout from "/game/layout/marginlayout.js";
import Button from "/game/component/button.js";
import GridLayout from "/game/layout/gridlayout.js";
import ArenaPlayerCard from "/game/component/arenaplayercard.js";
import ResourceManager from "/game/misc/resourcemanager.js";
import InteractiveImage from "/game/component/interactiveimage.js";
import Image from "/game/component/image.js";

export default class ArenaView {

    constructor(localeManager) {
        this.localeManager = localeManager;

        this.__build_layout__();
        this.__build_centerPanelGameOver__();
    }

    __build_layout__() {
        this.layout = new SplitLayout(false, 0.10, 0.90);
        this.layout.size = { width: document.body.clientWidth, height: document.body.clientHeight };

        this.playerCard = [];
        this.playerCardLayout = [];
        this.rowLayout = [];
        this.cardInHand = [];

        this.headerPanel = new SplitLayout(true, 0.66, 0.33);
        this.headerPanel.backgroundColor = "rgba(35, 35, 35, 0.65)";

        this.layout.setComponent(0, this.headerPanel);

        {
            this.logoRoundLayout = new SplitLayout(true);
            this.headerPanel.setComponent(0, this.logoRoundLayout);

            this.logoLayout = new MarginLayout({left:0.015, top:0, right:0, bottom:0});
            this.logoRoundLayout.setComponent(0, this.logoLayout);

            this.logoLabel = new Label(this.localeManager.locale.LOGO_LABEL, LabelAlignment.LEFT);
            this.logoLabel.fillStyle = "white";
            this.logoLabel.fontSize = 24;
            this.logoLayout.setComponent(0, this.logoLabel);

            this.roundLabel = new Label(this.localeManager.locale.ROUND + ' 1', LabelAlignment.CENTER);
            this.roundLabel.fillStyle = "white";
            this.roundLabel.fontSize = 24;
            this.logoRoundLayout.setComponent(1, this.roundLabel);

            this.leaveGameButtonLayout = new MarginLayout({ top:0.15, left:0.60, bottom:0.15, right:0 });
            this.headerPanel.setComponent(1, this.leaveGameButtonLayout);

            this.leaveGameButton = new Button(this.localeManager.locale.LEAVE_GAME);
            this.leaveGameButtonLayout.setComponent(0, this.leaveGameButton);
        }

        this.centerPanelWrapper = new BorderLayout();
        this.layout.setComponent(1, this.centerPanelWrapper);

        this.messageLabel = new Label("", LabelAlignment.CENTER);
        this.messageLabel.fillStyle = "white";
        this.centerPanelWrapper.setComponent(0, this.messageLabel);

        this.centerPanel = new GridLayout({rows: 5, cols: 1});
        this.centerPanelWrapper.setComponent(1, this.centerPanel);

        const playerCardLayoutMargin = { top:0.15, left:0.10, bottom:0.15, right:0.10 };
        const playerCardPreferredSize = { width: 250, height: 75 };

        // first row
        {
            this.rowLayout[0] = new GridLayout({rows: 1, cols: 5});
            this.centerPanel.setComponent({row: 0, col: 0}, this.rowLayout[0]);

            this.playerCardLayout[2] = new MarginLayout(playerCardLayoutMargin);
            this.rowLayout[0].setComponent({row: 0, col: 1}, this.playerCardLayout[2]);

            this.playerCard[2] = new ArenaPlayerCard(this.localeManager);
            this.playerCardLayout[2].setComponent(0, this.playerCard[2]);
            this.playerCard[2].unsetPlayer();
            this.playerCard[2].setPreferredSize(playerCardPreferredSize);

            this.playerCardLayout[3] = new MarginLayout(playerCardLayoutMargin);
            this.rowLayout[0].setComponent({row: 0, col: 3}, this.playerCardLayout[3]);

            this.playerCard[3] = new ArenaPlayerCard(this.localeManager);
            this.playerCardLayout[3].setComponent(0, this.playerCard[3]);
            this.playerCard[3].unsetPlayer();
            this.playerCard[3].setPreferredSize(playerCardPreferredSize);
        }

        // second row
        {
            let split1 = new SplitLayout(true, 0.25, 0.75);
            this.centerPanel.setComponent({row: 2, col: 0}, split1);

            this.playerCardLayout[1] = new MarginLayout(playerCardLayoutMargin);
            split1.setComponent(0, this.playerCardLayout[1]);

            this.playerCard[1] = new ArenaPlayerCard(this.localeManager);
            this.playerCardLayout[1].setComponent(0, this.playerCard[1]);
            this.playerCard[1].unsetPlayer();
            this.playerCard[1].setPreferredSize(playerCardPreferredSize);

            let split2 = new SplitLayout(true, 0.6666, 0.3333);
            split1.setComponent(1, split2);

            this.gamePanel = new GridLayout({rows: 1, cols: 5});
            split2.setComponent(0, this.gamePanel);

            // game panel
            {
                this.trumpCard = new InteractiveImage(ResourceManager.getAsset("blue-back"));
                this.gamePanel.setComponent({row:0, col:0}, this.trumpCard);

                this.remainingPack = new InteractiveImage(ResourceManager.getAsset("blue-back"));
                this.gamePanel.setComponent({row:0, col:1}, this.remainingPack);

                // empty

                this.droppedLastCard = new InteractiveImage(ResourceManager.getAsset("blue-back"));
                this.gamePanel.setComponent({row:0, col:3}, this.droppedLastCard);

                this.userDroppedCard = new InteractiveImage(ResourceManager.getAsset("blue-back"));
                this.gamePanel.setComponent({row:0, col:4}, this.userDroppedCard);
            }


            this.playerCardLayout[4] = new MarginLayout(playerCardLayoutMargin);
            split2.setComponent(1, this.playerCardLayout[4]);

            this.playerCard[4] = new ArenaPlayerCard(this.localeManager);
            this.playerCardLayout[4].setComponent(0, this.playerCard[4]);
            this.playerCard[4].unsetPlayer();
            this.playerCard[4].setPreferredSize(playerCardPreferredSize);
        }

        // third row
        {
            const userBtnMargins = { top:0.20, left:0.30, bottom:0.20, right:0.30 }

            this.rowLayout[2] = new GridLayout({rows: 1, cols: 3});
            this.centerPanel.setComponent({row: 4, col: 0}, this.rowLayout[2]);

            let dropButtonLayout = new MarginLayout(userBtnMargins);
            this.rowLayout[2].setComponent({row: 0, col: 0}, dropButtonLayout);

            this.dropButton = new Button(this.localeManager.locale.DROP);
            dropButtonLayout.setComponent(0, this.dropButton);

            this.playerHandLayout = new GridLayout({rows: 1, cols: 5});
            this.rowLayout[2].setComponent({row: 0, col: 1}, this.playerHandLayout);

            for (let i = 0; i < 5; ++i) {
                this.cardInHand[i] = new InteractiveImage(ResourceManager.getAsset("gray-back"));
                this.cardInHand[i].selected = false;
                this.playerHandLayout.setComponent({row:0, col:i}, this.cardInHand[i]);
            }

            let claimButtonLayout = new MarginLayout(userBtnMargins);
            this.rowLayout[2].setComponent({row: 0, col: 2}, claimButtonLayout);

            this.claimButton = new Button(this.localeManager.locale.CLAIM);
            claimButtonLayout.setComponent(0, this.claimButton);
        }
    }

    __build_centerPanelGameOver__() {
        this.centerPanelGameOver = new GridLayout({rows: 7, cols: 1});

        this.claimTitle = new Label(this.localeManager.locale.CLAIM.toUpperCase(), LabelAlignment.CENTER);
        this.claimTitle.fillStyle = "white";
        this.claimTitle.fontSize = 28;
        this.centerPanelGameOver.setComponent({row: 1, col: 0}, this.claimTitle);

        this.claimCaller = new Label(this.localeManager.locale.BY + " " + this.localeManager.locale.DEFAULT_USERNAME,
            LabelAlignment.CENTER);
        this.claimCaller.fillStyle = "white";
        this.claimCaller.fontSize = 28;
        this.centerPanelGameOver.setComponent({row: 2, col: 0}, this.claimCaller);

        this.winOrLoss = new Label("", LabelAlignment.CENTER);
        this.winOrLoss.fillStyle = "white";
        this.winOrLoss.fontSize = 28;
        this.centerPanelGameOver.setComponent({row:3, col: 0}, this.winOrLoss);

        this.winningHandLayout = new GridLayout({rows: 1, cols: 9});
        this.centerPanelGameOver.setComponent({row:4, col: 0}, this.winningHandLayout);

        this.winningCard = [];
        for (let i = 2; i < 7; ++i) {
            this.winningCard[i - 2] = new Image(ResourceManager.getAsset("purple-back"));
            this.winningHandLayout.setComponent({row:0, col: i}, this.winningCard[i - 2]);
        }

        this.otherWinners = new Label("", LabelAlignment.CENTER);
        this.otherWinners.fillStyle = "white";
        this.otherWinners.fontSize = 28;
        this.centerPanelGameOver.setComponent({row:5, col:0}, this.otherWinners);
    }

    setLeaveGameButtonConsumer(consumer) {
        this.leaveGameButton.onButtonClicked(consumer);
    }

    setRound(round, maxRounds) {
        this.roundLabel.text = this.localeManager.locale.ROUND + " " + round;
    }

    setDropButtonConsumer(consumer) {
        this.dropButton.onButtonClicked(consumer);
    }

    setClaimButtonConsumer(consumer) {
        this.claimButton.onButtonClicked(consumer);
    }

    setTrumpCard(asset) {
        this.trumpCard.setImage(ResourceManager.getAsset(asset));
    }

    setCurrentDroppedCard(asset) {
        this.droppedLastCard.setImage(ResourceManager.getAsset(asset));
    }

    setUserLastDroppedCard(asset) {
        this.userDroppedCard.setImage(ResourceManager.getAsset(asset));
    }

    setCardInHand(cardIndex, asset) {
        this.cardInHand[cardIndex].setImage(ResourceManager.getAsset(asset));
    }

    setCardsInHand(playerIndex, cardsCount) {
        if (playerIndex === 0)
            return;

        this.playerCard[playerIndex].setPlayerHand(cardsCount);
    }

    setTurn(playerIndex) {
        this.dropButton.visible = false;
        this.claimButton.visible = false;

        for (let i = 1; i < 5; ++i) {
            this.playerCard[i].strokeStyle = "rgba(255, 255, 255, 1)";
        }

        if (playerIndex === 0) {
            this.dropButton.visible = true;
            this.claimButton.visible = true;
        } else
            this.playerCard[playerIndex].strokeStyle = "rgba(189, 15, 151, 1)";
    }

    setOnCardSelectConsumer(card, selectConsumer, deselectConsumer) {
        this.cardInHand[card].onImageClicked((image) => {
            image.selected = !image.selected;

            if (image.selected) {
                image.position.y -= 10;
                image.__computeCenteredPosition__();

                selectConsumer();
            } else {
                image.position.y += 10;
                image.__computeCenteredPosition__();

                deselectConsumer();
            }
        });

    }

    selectCard(card) {
        if (!this.cardInHand[card].selected) {
            this.cardInHand[card].position.y -= 10;
            this.cardInHand[card].__computeCenteredPosition__();
            this.cardInHand[card].selected = true;
        }
    }

    deselectCard(card) {
        if (this.cardInHand[card].selected) {
            this.cardInHand[card].position.y += 10;
            this.cardInHand[card].__computeCenteredPosition__();
            this.cardInHand[card].selected = false;
        }
    }

    setOnDeckClickConsumer(consumer) {
        this.remainingPack.onImageClicked(consumer);
    }

    setOnDroppedLastCardClickConsumer(consumer) {
        this.droppedLastCard.onImageClicked(consumer);
    }

    setCardVisible(card, visible) {
        this.cardInHand[card].visible = visible;
    }

    setUserLastDroppedCardVisible(visible) {
        this.userDroppedCard.visible = visible;
    }

    clearConsumers() {
        this.setDropButtonConsumer(() => {});
        this.setClaimButtonConsumer(() => {});
        this.setOnDeckClickConsumer(() => {});
        this.setOnDroppedLastCardClickConsumer(() => {});

        for (let i = 0; i < 5; ++i) {
            this.cardInHand[i].onImageClicked(() => {});
        }
    }

    setMessage(message, timer = 3000) {
        this.messageLabel.text = message;

        setTimeout(() => { this.messageLabel.text = ""; }, timer);
    }

    putGameOverPanel() {
        this.layout.setComponent(1, this.centerPanelGameOver);
    }

    setClaimScore(score) {
        this.claimTitle.text = this.localeManager.locale.CLAIM.toUpperCase() + " "
            + this.localeManager.locale.SCORE.toUpperCase() + " "
            + score;
    }

    setClaimCaller(username) {
        this.claimCaller.text = this.localeManager.locale.BY.toUpperCase() + " " + username;
    }

    setCallerWins(callerWins, realWinner) {
        if (callerWins) {
            this.winOrLoss.text = this.localeManager.locale.WINNER_MESSAGE.toUpperCase();
        } else {
            this.winOrLoss.text = this.localeManager.locale.LOSER_MESSAGE_BEFORE.toUpperCase()
                + " " + realWinner + " "
                + this.localeManager.locale.LOSER_MESSAGE_AFTER.toUpperCase();
        }
    }

    setWinningCardVisible(cardIndex, visible) {
        this.winningCard[cardIndex].visible = visible;
    }

    setWinningCard(cardIndex, assetId) {
        this.winningCard[cardIndex].setImage(ResourceManager.getAsset(assetId));
    }

    setOtherWinners(otherWinners) {
        if (otherWinners.length === 0) {
            return;
        }

        let winnersNames = "";
        for (let i = 0; i < otherWinners.length; ++i) {
            winnersNames += otherWinners[i] + ", ";
        }
        winnersNames = winnersNames.substr(0, winnersNames.length - 2);

        this.otherWinners.text = this.localeManager.locale.OTHER_WINNERS.toUpperCase() + " " + winnersNames;
    }

    // TODO: Drag and drop for drop in the future
}