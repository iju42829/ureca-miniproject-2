package com.ureca.miniproject.test;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitForSelectorState;

public class MafiaE2ETest {
    public static void main(String[] args) {
        Playwright playwright = Playwright.create();
        BrowserType browserType = playwright.chromium();

        Page playerOnePage = null;

        {
            String name = "test1";
            String email = name + "@naver.com";
            String password = "1234";
            Path userDataDir = Paths.get("user-data/user1");

            BrowserContext context = browserType.launchPersistentContext(userDataDir,
                new BrowserType.LaunchPersistentContextOptions().setHeadless(false));
            Page page = context.pages().get(0);

            try {
                page.navigate("http://localhost:8080/register.html");
                page.fill("#name", name);
                page.fill("#email", email);
                page.fill("#password", password);
                page.click("#btnRegister");
                page.waitForTimeout(500);

                page.waitForURL("**/login");
                page.fill("#email", email);
                page.fill("#password", password);
                page.click("#btnLogin");
                page.waitForTimeout(1000);
                page.waitForURL("**/index.html");

                page.click("#btnCreateGame");
                page.waitForURL("**/createGameRoom.html");
                page.fill("#title", "테스트방1");
                page.fill("#maxPlayer", "6");
                page.click("button[type='submit']");
                page.waitForTimeout(1000);

                playerOnePage = page;
                System.out.println("✅ test1 방 생성 완료");
            } catch (Exception e) {
                System.out.println("❌ test1 실패: " + e.getMessage());
            }
        }

        for (int i = 2; i <= 6; i++) {
            String name = "test" + i;
            String email = name + "@naver.com";
            String password = "1234";
            Path userDataDir = Paths.get("user-data/user" + i);

            BrowserContext context = browserType.launchPersistentContext(userDataDir,
                new BrowserType.LaunchPersistentContextOptions().setHeadless(false));
            Page page = context.pages().get(0);

            try {
                page.navigate("http://localhost:8080/register.html");
                page.fill("#name", name);
                page.fill("#email", email);
                page.fill("#password", password);
                page.click("#btnRegister");
                page.waitForTimeout(500);

                page.waitForURL("**/login");
                page.fill("#email", email);
                page.fill("#password", password);
                page.click("#btnLogin");
                page.waitForTimeout(1000);
                page.waitForURL("**/index.html");

                page.click("#btnGameList");
                page.waitForURL("**/game/gameRoomList.html");
                page.waitForSelector(".btn-join");
                page.click(".btn-join");

                System.out.println("✅ " + name + " 참가 완료");
            } catch (Exception e) {
                System.out.println("❌ " + name + " 실패: " + e.getMessage());
            }
        }

        if (playerOnePage != null) {
            playerOnePage.onDialog(dialog -> {
                System.out.println("⚠️ ALERT: " + dialog.message());
                dialog.accept();
            });

            playerOnePage.waitForTimeout(1000); 
            playerOnePage.waitForSelector("#startBtn", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
            playerOnePage.click("#startBtn");
            playerOnePage.waitForTimeout(1000);
            System.out.println("🚀 test1 게임 시작 버튼 클릭 완료");
        }

        System.out.println("\n🎉 모든 유저 처리 완료! 브라우저는 직접 종료하세요.");
    }
}
