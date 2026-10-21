const API = {
  token: () => localStorage.getItem("token") || "",
  user: () => JSON.parse(localStorage.getItem("user") || "null"),
  async request(url, options = {}) {
    const res = await fetch(url, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        "Authorization": API.token(),
        ...(options.headers || {})
      }
    });
    const data = await res.json();
    if (!data.success) throw new Error(data.message || "请求失败");
    return data.data;
  },
  get(url) { return API.request(url); },
  post(url, body) { return API.request(url, { method: "POST", body: JSON.stringify(body || {}) }); },
  put(url, body) { return API.request(url, { method: "PUT", body: JSON.stringify(body || {}) }); },
  del(url) { return API.request(url, { method: "DELETE" }); },
  logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    location.href = "/index.html";
  }
};

function money(job) {
  return job.salaryRange || "薪资面议";
}

function fmtTime(text) {
  return text ? String(text).replace("T", " ").slice(0, 16) : "";
}

function navHtml() {
  const user = API.user();
  if (!user) return `<a href="/index.html">首页</a><a href="/rank.html">浏览量排行</a><a href="/login.html">注册登录</a>`;
  if (user.role === "ADMIN") return `<a href="/admin/index.html">管理后台</a><button onclick="API.logout()">退出</button>`;
  if (user.role === "RECRUITER") return `<a href="/hr/index.html">招聘者中心</a><button onclick="API.logout()">退出</button>`;
  return `<a href="/index.html">首页</a><a href="/user/recommend.html">职位推荐</a><a href="/user/resume.html">我的简历</a><a href="/user/favorites.html">我的收藏</a><a href="/user/applications.html">我的投递</a><a href="/user/ratings.html">我的评分</a><a href="/user/profile.html">个人信息</a><a href="/rank.html">浏览量排行</a><button onclick="API.logout()">退出</button>`;
}

function mountTopbar() {
  document.querySelector(".topbar").innerHTML = `<div class="brand">智能招聘匹配系统</div><nav class="nav">${navHtml()}</nav>`;
}

function jobCard(job, extra = "") {
  return `<div class="card">
    <img class="job-img" src="${job.imageUrl || '/css/job.svg'}" alt="${job.title}">
    <h3>${job.title}</h3>
    <div class="meta"><span>${money(job)}</span><span>${job.location || ""}</span><span>${job.education || ""}</span><span>${job.vacancyCount || 0}人</span></div>
    <p>${(job.description || "").slice(0, 72)}...</p>
    <div class="meta"><span>浏览 ${job.viewCount || 0}</span><span>评分 ${job.ratingAverage || 0}</span>${extra}</div>
    <a class="btn secondary" href="/detail.html?id=${job.id}">查看详情</a>
  </div>`;
}

function jobRow(job) {
  return `<tr>
    <td>${job.title}</td><td>${money(job)}</td><td>${job.location || ""}</td>
    <td>${job.vacancyCount || 0}</td><td>${job.education || ""}</td>
    <td><a class="btn secondary" href="/detail.html?id=${job.id}">查看详情</a></td>
  </tr>`;
}

function sidebar(role, active) {
  const userItems = [["recommend.html", "职位推荐"], ["resume.html", "我的简历"], ["favorites.html", "我的收藏"], ["applications.html", "我的投递"], ["ratings.html", "我的评分"], ["profile.html", "个人信息"]];
  const adminItems = [["index.html", "首页"], ["manage.html?type=applications", "投递信息"], ["manage.html?type=favorites", "收藏信息"], ["manage.html?type=hotJobs", "热门职位"], ["manage.html?type=users", "用户信息"], ["manage.html?type=categories", "类别信息"], ["manage.html?type=jobs", "职位信息"], ["manage.html?type=ratings", "评分信息"], ["manage.html?type=comments", "评论信息"], ["manage.html?type=groups", "组"]];
  const hrItems = [["index.html", "数据看板"], ["jobs.html", "岗位管理"], ["applications.html", "简历管理"], ["interviews.html", "面试安排"], ["company.html", "公司信息"]];
  const items = role === "admin" ? adminItems : role === "hr" ? hrItems : userItems;
  return `<aside class="sidebar">${items.map(([href, name]) => `<a class="${active === href.split("?")[0] ? "active" : ""}" href="${href}">${name}</a>`).join("")}<a href="/index.html">返回前台</a></aside>`;
}
