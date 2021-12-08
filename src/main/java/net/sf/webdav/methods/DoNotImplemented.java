package net.sf.webdav.methods;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.webdav.IMethodExecutor;
import net.sf.webdav.ITransaction;
import net.sf.webdav.WebdavStatus;

public class DoNotImplemented implements IMethodExecutor {

    private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory
            .getLogger(DoNotImplemented.class);
    private boolean _readOnly;

    public DoNotImplemented(boolean readOnly) {
        _readOnly = readOnly;
    }

    public void execute(ITransaction transaction, HttpServletRequest req,
            HttpServletResponse resp) throws IOException {
        LOG.trace("-- " + req.getMethod());

        if (_readOnly) {
            resp.sendError(WebdavStatus.SC_FORBIDDEN);
        } else
            resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
}
