<#import "parts/common.ftl" as c>

<@c.page>
    <div>
        <div class="form-row mb-3">
            <form method="get" action="/main" class="form-inline">
                <input class="form-control" type="text" name="filter" placeholder="enter filter" value="<#if filter??>${filter}</#if>">
                <button type="submit" class="ml-3 btn btn-primary">Search</button>
            </form>
        </div>

        <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
            Add new message
        </a>

        <div class="form-group mt-3">
            <form method="post" class="collapse" id="collapseExample" enctype="multipart/form-data">
                <div class="form-group">
                    <input class="form-control" type="text" name="text" placeholder="enter message">
                </div>
                <div class="form-group">
                    <input class="form-control" type="text" name="tag" placeholder="enter tag">
                </div>
                <div class="form-group">
                    <div class="custom-file">
                        <input type="file" name="file" id="customFile">
                        <label class="custom-file-label" for="customFile">Choose file</label>
                    </div>
                </div>
                <div class="form-group">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <button class="btn btn-primary" type="submit">Submit</button>
                </div>
            </form>
        </div>
    </div>

<div class="card-columns">
    <#list messages as message>
        <div class="card my-3">
            <div>
        <#if message.filename??>
            <img class="card-img-top" src="/img/${message.filename}" alt="">
        </#if>
            </div>
            <div class="m-2">
                <span>${message.text}</span>
            </div>
            <i>${message.tag}</i>
            <div class="card-footer text-muted">
                <strong>${message.authorName}</strong>
            </div>

        </div>
    <#else>
            No message
    </#list>
</div>

</@c.page>