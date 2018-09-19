
    <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
        Message editor
    </a>
        <div class="form-group mt-3">
            <form method="post" class="collapse <#if message??>Show</#if>" id="collapseExample" enctype="multipart/form-data">
                <div class="form-group">
                    <input class="form-control ${(textError??)?string('is-invalid','')}"
                           value="<#if message??>${message.text}</#if>" type="text" name="text" placeholder="enter message">
                    <#if textError??>
                        <div class="invalid-feedback">
                            ${textError}
                        </div>
                    </#if>
                </div>
                <div class="form-group">
                    <input class="form-control" type="text" name="tag"
                           value="<#if message??>${message.tag}</#if>"placeholder="enter tag">
                    <#if textError??>
                        <div class="invalid-feedback">
                            ${tagError}
                        </div>
                    </#if>
                </div>
                <div class="form-group">
                    <div class="custom-file">
                        <input type="file" name="file" id="customFile">
                        <label class="custom-file-label" for="customFile">Choose file</label>
                    </div>
                </div>
                <div class="form-group">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <input type="hidden" name="id" value="<#if message??>${message.id}</#if>">
                    <button class="btn btn-primary" type="submit">Save</button>
                </div>
            </form>
        </div>