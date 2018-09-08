<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <div>
    <@l.logout/>
        <form method="post">
            <input type="text" name="text" placeholder="enter message">
            <input type="text" name="tag" placeholder="enter tag">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit">Submit</button>
        </form>
        <form method="get" action="/main">
            <input type="text" name="filter" placeholder="enter filter" value="${filter}">
            <button type="submit">Filter</button>
        </form>
    </div>
      <div>List of messages</div>
    <#list messages as message>
  <div>
      <b>${message.id}</b>
      <span>${message.text}</span>
      <i>${message.tag}</i>
      <strong>${message.authorName}</strong>
  </div>
    <#else>
            No message
    </#list>
</@c.page>