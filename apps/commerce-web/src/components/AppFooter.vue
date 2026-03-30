<script setup lang="ts">
</script>

<template>
  <footer class="footer">
    <div class="footer-inner">
      <div class="footer-top">
        <div class="footer-brand">
          <h3 class="footer-logo">LOOPERS</h3>
          <p class="footer-desc">트렌디한 패션을 합리적인 가격에</p>
        </div>
        <div class="footer-links">
          <div class="link-group">
            <h4>고객센터</h4>
            <a href="#">자주 묻는 질문</a>
            <a href="#">1:1 문의</a>
            <a href="#">공지사항</a>
          </div>
          <div class="link-group">
            <h4>회사 정보</h4>
            <a href="#">회사 소개</a>
            <a href="#">이용약관</a>
            <a href="#">개인정보처리방침</a>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2026 Loopers. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background: var(--gray-900);
  color: var(--gray-400);
  margin-top: auto;
}

.footer-inner {
  max-width: var(--max-width);
  margin: 0 auto;
  padding: 40px 20px 24px;
}

.footer-top {
  display: flex;
  justify-content: space-between;
  gap: 40px;
  padding-bottom: 24px;
  border-bottom: 1px solid var(--gray-700);
}

.footer-logo {
  font-size: 20px;
  font-weight: 900;
  color: var(--white);
  margin-bottom: 8px;
}

.footer-desc {
  font-size: 13px;
}

.footer-links {
  display: flex;
  gap: 48px;
}

.link-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.link-group h4 {
  font-size: 13px;
  font-weight: 700;
  color: var(--gray-300);
  margin-bottom: 4px;
}

.link-group a {
  font-size: 13px;
  color: var(--gray-500);
  transition: color 0.15s;
}

.link-group a:hover {
  color: var(--gray-300);
}

.footer-bottom {
  padding-top: 20px;
  font-size: 12px;
  color: var(--gray-600);
}

@media (max-width: 768px) {
  .footer-top {
    flex-direction: column;
    gap: 24px;
  }
}
</style>
