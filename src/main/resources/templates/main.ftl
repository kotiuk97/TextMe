<#import "parts/common.ftl" as c>

<@c.page>
    <div>
        <div class="form-row mb-3">
            <form method="get" action="/main" class="form-inline">
                <input class="form-control" type="text" name="filter" placeholder="enter filter" value="<#if filter??>${filter}</#if>">
                <button type="submit" class="ml-3 btn btn-primary">Search</button>
            </form>
        </div>

<#include "parts/messageEdit.ftl">
    </div>

<#include "parts/messageList.ftl">



</@c.page>